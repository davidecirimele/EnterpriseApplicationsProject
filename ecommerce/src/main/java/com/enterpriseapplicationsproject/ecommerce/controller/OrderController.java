package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.dao.OrdersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/orders", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersDao orderDao;

    private final OrdersService ordersService;

    @PostMapping(consumes = "application/json", path = "/add")
    public ResponseEntity<OrderDto> addOrder(@RequestBody  OrderDto orderDto) {
        OrderDto addedOrder = ordersService.addOrder(orderDto);
        return new ResponseEntity<>(addedOrder, HttpStatus.CREATED);
    }

    @GetMapping(consumes = "application/json", path = "/get/{userId}")
    public ResponseEntity<List<OrderDto>> getAllUserOrders(@PathVariable Long userId) {
        List<OrderDto> orders = ordersService.getAllOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json", path = "/cancel/{orderId}")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
        OrderDto cancelledOrder = ordersService.setOrderStatusToCancelled(orderId);
        return new ResponseEntity<>(cancelledOrder, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/confirmed/{userId}")
    public ResponseEntity<List<OrderDto>> getAllConfirmedOrders(@PathVariable Long userId) {
        List<OrderDto> orders = ordersService.getAllConfirmedOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/cancelled/{userId}")
    public ResponseEntity<List<OrderDto>> getAllCancelledOrders(@PathVariable Long userId) {
        List<OrderDto> orders = ordersService.getAllCancelledOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }




}
