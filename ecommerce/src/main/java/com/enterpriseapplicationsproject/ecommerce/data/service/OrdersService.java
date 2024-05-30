package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;

import java.util.List;

public interface OrdersService {

    OrderDto addOrder(OrderDto orderDto);

    List<OrderDto> getAllOrdersByUserId(Long userId);

    OrderDto setOrderStatusToCancelled(Long orderId);

    List<OrderDto> getAllConfirmedOrdersByUserId(Long userId);

}
