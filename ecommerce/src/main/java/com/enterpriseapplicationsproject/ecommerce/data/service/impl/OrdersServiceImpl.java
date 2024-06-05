package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.OrdersDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.PaymentMethodsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ProductsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.enterpriseapplicationsproject.ecommerce.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersDao ordersDao;

    private final ModelMapper modelMapper;

    private final UsersDao usersDao;

    private final PaymentMethodsDao paymentMethodsDao;

    private final ProductsDao productDao;





    @Override
    public OrderDto addOrder(OrderDto orderDto) {

        Order order = modelMapper.map(orderDto, Order.class);
        User user = usersDao.findById(orderDto.getUser().getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);
        PaymentMethod paymentMethod = paymentMethodsDao.findById(orderDto.getPaymentMethod().getPaymentMethodId()).orElseThrow(() -> new RuntimeException("Payment method not found"));
        order.setPaymentMethod(paymentMethod);

        List<OrderItem> orderItems = orderDto.getOrderItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setQuantity(itemDTO.getQuantity());

            Product product = productDao.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            item.setProduct(product);

            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOr = ordersDao.save(order);
        return modelMapper.map(savedOr, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrdersByUserId(Long userId) {
        List<Order> orders = ordersDao.findAllByUserId(userId);


        return orders.stream().map( o -> modelMapper.map(o, OrderDto.class)).toList();
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
