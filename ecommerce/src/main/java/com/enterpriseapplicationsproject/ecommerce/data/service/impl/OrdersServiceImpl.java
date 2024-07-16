package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.*;
import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
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

    private final ShoppingCartsDao shoppingCartDao;


    @Override
    public SaveOrderDto addOrder(CheckoutRequestDto orderDto) {
        User user = usersDao.findById(orderDto.getUserId().getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        ShoppingCart shoppingCart = shoppingCartDao.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Shopping cart  not found"));
        List<OrderItem> orderItems = shoppingCart.getShoppingCartItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());
        Order order = modelMapper.map(orderDto, Order.class);
        System.out.println("Address:" + order.getAddress());
        System.out.println("OrderDto: " + orderDto.toString());
        if (!validateOrder(orderDto)) {
            throw new RuntimeException("Not enough stock for some products");
        }




        order.setUser(user);


        List<OrderItem> orderItems = orderDto.getOrderItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setQuantity(itemDTO.getQuantity());


            Product product = productDao.findById(itemDTO.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            item.setProduct(product);

            return item;
            }).collect(Collectors.toList());

            order.setOrderItems(orderItems);
            Order savedOr = ordersDao.save(order);
            return modelMapper.map(savedOr, SaveOrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrdersByUserId(Long userId) {
        List<Order> orders = ordersDao.findAllByUserId(userId, Sort.by(Sort.Order.desc("ordedDate")));


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
        List<Order> orders = ordersDao.findAllConfirmedOrdersByUserId(userId, Sort.by(Sort.Order.desc("orderDate")));
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }

    @Override
    public List<OrderDto> getAllCancelledOrdersByUserId(Long userId) {
        List<Order> orders = ordersDao.findAllCancelledOrdersByUserId(userId, Sort.by(Sort.Order.desc("orderDate")));
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }



    private boolean validateOrder(SaveOrderDto orderDto) {
        for (SaveOrderItemDto item : orderDto.getOrderItems()) {
            Product product = productDao.findById(item.getProduct().getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getStock() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }
}
