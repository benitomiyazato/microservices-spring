package com.benitomiyazato.orderservice.service;

import com.benitomiyazato.orderservice.dto.InventoryResponse;
import com.benitomiyazato.orderservice.dto.OrderRequest;
import com.benitomiyazato.orderservice.dto.OrderResponse;
import com.benitomiyazato.orderservice.event.OrderPlacedEvent;
import com.benitomiyazato.orderservice.model.Order;
import com.benitomiyazato.orderservice.model.OrderLine;
import com.benitomiyazato.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderResponse newOrder(OrderRequest orderRequest) {
        Order orderToSave = new Order();
        BeanUtils.copyProperties(orderRequest, orderToSave);

        List<BigDecimal> prices = new ArrayList<>();
        for (OrderLine orderLine : orderRequest.getOrderLines()) {
            prices.add(orderLine.getPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity())));
        }

        BigDecimal totalPrice = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        orderToSave.setTotalPrice(totalPrice);

        List<String> names = orderRequest.getOrderLines().stream().map(OrderLine::getName).toList();
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get().uri("http://inventory-service/api/inventories", uriBuilder -> uriBuilder.queryParam("name", names).build()).retrieve().bodyToMono(InventoryResponse[].class).block();
        ;


        boolean allProductsInStock = false;
        if (inventoryResponses != null && inventoryResponses.length > 0) {
            allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        }

        if (allProductsInStock) {
            Order savedOrder = orderRepository.save(orderToSave);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(savedOrder.getId()));

            OrderResponse orderResponse = new OrderResponse();
            BeanUtils.copyProperties(savedOrder, orderResponse);
            return orderResponse;
        }
        return null;
    }
}
