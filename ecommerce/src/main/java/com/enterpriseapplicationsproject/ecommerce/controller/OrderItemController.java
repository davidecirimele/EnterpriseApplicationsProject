package com.enterpriseapplicationsproject.ecommerce.controller;


import com.enterpriseapplicationsproject.ecommerce.data.service.OrderItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/orderItems", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor

public class OrderItemController {

    private final OrderItemsService orderItemsService;

    @GetMapping(consumes = "application/json", path = "/get/{orderId}/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId, @PathVariable UUID userId) {
        List<OrderItemDto> orderItems = orderItemsService.getOrderItemsByOrderId(orderId, userId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }
}
