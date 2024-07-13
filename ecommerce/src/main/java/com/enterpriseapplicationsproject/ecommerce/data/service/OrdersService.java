package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveOrderDto;

import java.util.List;

public interface OrdersService {

    SaveOrderDto addOrder(SaveOrderDto orderDto);

    List<OrderDto> getAllOrdersByUserId(Long userId);

    OrderDto setOrderStatusToCancelled(Long orderId);

    List<OrderDto> getAllConfirmedOrdersByUserId(Long userId);

    List<OrderDto> getAllCancelledOrdersByUserId(Long userId);

}
