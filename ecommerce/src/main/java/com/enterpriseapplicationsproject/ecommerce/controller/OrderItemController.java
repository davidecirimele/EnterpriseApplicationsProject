package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrderItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/orderItems", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor

public class OrderItemController {

    private final OrderItemsService orderItemsService;

    @RateLimit(type ="USER")
    @GetMapping(consumes = "application/json", path = "/get/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDto> orderItems = orderItemsService.getOrderItemsByOrderId(orderId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }
}
