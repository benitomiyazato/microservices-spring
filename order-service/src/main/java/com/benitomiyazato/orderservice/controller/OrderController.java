package com.benitomiyazato.orderservice.controller;

import com.benitomiyazato.orderservice.dto.OrderRequest;
import com.benitomiyazato.orderservice.dto.OrderResponse;
import com.benitomiyazato.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public ResponseEntity<Object> newOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.newOrder(orderRequest);
        if(orderResponse == null) {
            return new ResponseEntity<>(HttpStatus.valueOf(422));
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> fallbackMethod (OrderRequest orderRequest, RuntimeException exception) {
        return ResponseEntity.badRequest().body("Seems like we have a problem, please try again later!");
    }
}
