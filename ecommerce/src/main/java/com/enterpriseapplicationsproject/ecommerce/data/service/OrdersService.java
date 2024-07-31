package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.CheckoutRequestDto;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveOrderDto;

import java.util.List;
import java.util.UUID;

public interface OrdersService {

    SaveOrderDto addOrder(CheckoutRequestDto orderDto);

    List<OrderDto> getAllOrdersByUserId(UUID userId);

    OrderDto setOrderStatusToCancelled(Long orderId);

    List<OrderDto> getAllConfirmedOrdersByUserId(UUID userId);

    List<OrderDto> getAllCancelledOrdersByUserId(UUID userId);


}
