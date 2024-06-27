package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.OrderItemDto;

import java.util.List;

public interface OrderItemsService {

    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId);
}
