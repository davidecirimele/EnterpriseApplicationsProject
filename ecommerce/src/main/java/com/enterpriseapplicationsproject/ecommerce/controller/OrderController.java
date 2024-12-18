package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.dao.OrdersDao;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import jakarta.validation.Valid;
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
@RequestMapping(path = "/api/v1/orders", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    @PostMapping(consumes = "application/json", path = "/add")
    @PreAuthorize("#orderDto.userId.userId == authentication.principal.getId()")
    public ResponseEntity<SaveOrderDto> addOrder(@Valid @RequestBody CheckoutRequestDto orderDto) {
         SaveOrderDto addedOrder = ordersService.addOrder(orderDto);
        return new ResponseEntity<>(addedOrder, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<OrderSummaryDto>> getAllUserOrders(@PathVariable UUID userId) {
        List<OrderSummaryDto> orders = ordersService.getAllOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(path = "/purchased-products/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<BookDto>> getProductsByUserId(@PathVariable UUID userId) {
        List<BookDto> orders = ordersService.getProductsByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @PutMapping(consumes = "application/json", path = "/cancel/{orderId}/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId , @PathVariable UUID userId) {
         OrderDto cancelledOrder = ordersService.setOrderStatusToCancelled(orderId, userId);
        return new ResponseEntity<>(cancelledOrder, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/confirmed/{userId}")
    @PreAuthorize("#userId == authentication.credentials or hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllConfirmedOrders(@PathVariable UUID userId) {
        List<OrderDto> orders = ordersService.getAllConfirmedOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/cancelled/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllCancelledOrders(@PathVariable UUID userId) {
        List<OrderDto> orders = ordersService.getAllCancelledOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }




}
