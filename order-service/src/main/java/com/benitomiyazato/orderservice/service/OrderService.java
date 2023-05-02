package com.benitomiyazato.orderservice.service;

import com.benitomiyazato.orderservice.dto.InventoryResponse;
import com.benitomiyazato.orderservice.dto.OrderRequest;
import com.benitomiyazato.orderservice.dto.OrderResponse;
import com.benitomiyazato.orderservice.model.Order;
import com.benitomiyazato.orderservice.model.OrderLine;
import com.benitomiyazato.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public ResponseEntity<Object> newOrder(OrderRequest orderRequest) {
        Order orderToSave = new Order();
        BeanUtils.copyProperties(orderRequest, orderToSave);

        List<BigDecimal> prices = new ArrayList<>();
        for (OrderLine orderLine : orderRequest.getOrderLines()) {
            prices.add(orderLine.getPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity())));
        }

        BigDecimal totalPrice = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        orderToSave.setTotalPrice(totalPrice);

        List<String> names = orderRequest.getOrderLines().stream().map(OrderLine::getName).toList();
        InventoryResponse[] inventoryResponses = webClient.get().uri("http://localhost:8082/api/inventories/", uriBuilder -> uriBuilder.queryParam("name", names).build()).retrieve().bodyToMono(InventoryResponse[].class).block();
        ;


        boolean allProductsInStock = false;
        if (inventoryResponses != null && inventoryResponses.length > 0) {
            allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        }

        if (allProductsInStock) {
            Order savedOrder = orderRepository.save(orderToSave);
            OrderResponse orderResponse = new OrderResponse();
            BeanUtils.copyProperties(savedOrder, orderResponse);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Product is not in stock", HttpStatusCode.valueOf(422));
    }
}
