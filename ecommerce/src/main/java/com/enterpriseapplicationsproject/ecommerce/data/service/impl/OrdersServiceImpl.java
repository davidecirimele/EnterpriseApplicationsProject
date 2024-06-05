package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.OrdersDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.PaymentMethodsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersDao ordersDao;

    private final ModelMapper modelMapper;

    private final UsersDao usersDao;

    private final PaymentMethodsDao paymentMethodsDao;





    @Override
    public OrderDto addOrder(OrderDto orderDto) {

        System.out.println("pre saving id address:" + orderDto.getAddress().getId());
        System.out.println("pre saving id payments:" + orderDto.getPaymentMethod());
        System.out.println("pre saving id item:" + orderDto.getOrderItems().get(0).getOrderItemId());


        Order order = modelMapper.map(orderDto, Order.class);
        User user = usersDao.findById(orderDto.getUser().getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);
        PaymentMethod paymentMethod = paymentMethodsDao.findById(orderDto.getPaymentMethod().getPaymentMethodId()).orElseThrow(() -> new RuntimeException("Payment method not found"));
        order.setPaymentMethod(paymentMethod);
        System.out.println("id address:" + order.getAddress().getId());
        System.out.println("id user:" + order.getUser().getId());
        System.out.println("id payment:" + order.getPaymentMethod().getPaymentMethodId());
        System.out.println("id orderItems:" + order.getOrderItems().get(0).getOrderItemId());
        System.out.println("street: " + order.getAddress().);
        Order o = ordersDao.save(order);
        return modelMapper.map(o, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrdersByUserId(Long userId) {
        List<Order> orders = ordersDao.findAllByUserId(userId);
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }

    @Override
    public OrderDto setOrderStatusToCancelled(Long orderId) {
        Order order = ordersDao.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(OrderStatus.CANCELLED);
        Order o = ordersDao.save(order);
        return modelMapper.map(o, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllConfirmedOrdersByUserId(Long userId) {
        List<Order> orders = ordersDao.findAllConfirmedOrdersByUserId(userId);
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }

    @Override
    public List<OrderDto> getAllCancelledOrdersByUserId(Long userId) {
        List<Order> orders = ordersDao.findAllCancelledOrdersByUserId(userId);
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }






}
