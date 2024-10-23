package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.OrderItemDto;

import java.util.List;
import java.util.UUID;

public interface OrderItemsService {


    List<OrderItemDto> getOrderItemsByOrderId(Long orderId, UUID userId);
}
