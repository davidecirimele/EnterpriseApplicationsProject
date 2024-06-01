package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.OrderItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.OrderItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrderItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl  implements OrderItemsService {

    private final OrderItemsDao orderItemsDao;

    private final ModelMapper modelMapper;


    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemsDao.findAllByOrderId(orderId);
        return orderItems.stream().map(oi -> modelMapper.map(oi, OrderItemDto.class)).toList();
    }
}
