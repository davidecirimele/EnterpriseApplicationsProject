package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrdersService {

    SaveOrderDto addOrder(CheckoutRequestDto orderDto);

    List<OrderSummaryDto> getAllOrdersByUserId(UUID userId);

    List<BookDto> getProductsByUserId(UUID userId);

    OrderDto setOrderStatusToCancelled(Long orderId, UUID userId);

    List<OrderDto> getAllConfirmedOrdersByUserId(UUID userId);

    List<OrderDto> getAllCancelledOrdersByUserId(UUID userId);

    Page<OrderSummaryDto> getAll(Pageable pageable);
}
