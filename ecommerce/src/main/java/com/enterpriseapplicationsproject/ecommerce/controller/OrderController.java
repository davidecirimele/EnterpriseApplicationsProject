package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.dao.OrdersDao;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.dto.CheckoutRequestDto;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveOrderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping(consumes = "application/json", path = "/get/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllUserOrders(@PathVariable UUID userId) {
        List<OrderDto> orders = ordersService.getAllOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json", path = "/cancel/{orderId}")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
         OrderDto cancelledOrder = ordersService.setOrderStatusToCancelled(orderId);
        return new ResponseEntity<>(cancelledOrder, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/confirmed/{userId}")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
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
