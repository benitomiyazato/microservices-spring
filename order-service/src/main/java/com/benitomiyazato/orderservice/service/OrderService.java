package com.benitomiyazato.orderservice.service;

import com.benitomiyazato.orderservice.dto.OrderRequest;
import com.benitomiyazato.orderservice.dto.OrderResponse;
import com.benitomiyazato.orderservice.model.Order;
import com.benitomiyazato.orderservice.model.OrderLine;
import com.benitomiyazato.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse newOrder(OrderRequest orderRequest) {
        Order orderToSave = new Order();
        BeanUtils.copyProperties(orderRequest, orderToSave);

        List<BigDecimal> prices = new ArrayList<>();
        for (OrderLine orderLine : orderRequest.getOrderLines()) {
            prices.add(orderLine.getPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity())));
        }

        BigDecimal totalPrice = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        orderToSave.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(orderToSave);
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(savedOrder, orderResponse);
        return orderResponse;
    }
}
