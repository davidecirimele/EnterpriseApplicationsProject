package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderWithItemsIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveOrderDto;

import java.util.List;

public interface OrdersService {

    OrderDto addOrder(SaveOrderDto orderDto);

    List<OrderWithItemsIdDto> getAllOrdersByUserId(Long userId);

    OrderWithItemsIdDto setOrderStatusToCancelled(Long orderId);

    List<OrderWithItemsIdDto> getAllConfirmedOrdersByUserId(Long userId);

    List<OrderWithItemsIdDto> getAllCancelledOrdersByUserId(Long userId);

}
