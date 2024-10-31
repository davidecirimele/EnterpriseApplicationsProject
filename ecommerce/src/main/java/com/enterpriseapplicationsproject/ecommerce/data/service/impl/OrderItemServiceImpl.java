package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.OrderItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.OrderItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrderItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderItemDto;
import com.enterpriseapplicationsproject.ecommerce.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl  implements OrderItemsService {

    private final OrderItemsDao orderItemsDao;

    private final ModelMapper modelMapper;


    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId, UUID userId) {

        List<OrderItem> orderItems = orderItemsDao.findAllByOrderOrderIdAndUserId(orderId, userId);
        if (orderItems.isEmpty()) {
            throw  new OrderNotFoundException("Order not found");
        }
        return orderItems.stream().map(oi -> modelMapper.map(oi, OrderItemDto.class)).toList();
    }
}
