package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.*;
import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.data.service.ProductsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionsService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersDao ordersDao;

    private final ModelMapper modelMapper;

    private final UsersDao usersDao;

    private final PaymentMethodsDao paymentMethodsDao;

    private final ProductsDao productsDao;

    private final ShoppingCartsDao shoppingCartDao;

    private final AddressesDao addressesDao;

    private final ProductsService productsService;

    private final TransactionsDao transactionsDao;

    private final TransactionsService  transactionsService;


    @Override
    @Transactional
    public SaveOrderDto addOrder(CheckoutRequestDto orderDto) {


        User user = usersDao.findById(orderDto.getUserId().getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        ShoppingCart shoppingCart = shoppingCartDao.findByUserId(orderDto.getUserId().getUserId()).orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found"));
        addressesDao.findById(orderDto.getAddress().getId()).orElseThrow(() -> new AddressNotFoundException("Address not found"));
        PaymentMethod paymentMethod = paymentMethodsDao.findById(orderDto.getPaymentMethodId().getPaymentMethodId()).orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found"));
        if (!validateOrder(shoppingCart)) {
            throw new OutOfStockException("Products out of stock");
        }
        Order order = setOrder(shoppingCart, user, paymentMethod);
        Order savedOr = ordersDao.save(order);
        updateProductStock(shoppingCart);
        Transaction transaction = transactionsService.addTransaction(user, savedOr, paymentMethod, savedOr.getTotalAmount(), PaymentStatus.APPROVED, LocalDate.now());
        transactionsDao.save(transaction);
        return modelMapper.map(savedOr, SaveOrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrdersByUserId(Long userId) {
        List<Order> orders = ordersDao.findAllByUserId(userId, Sort.by(Sort.Order.desc("ordedDate")));
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }

    @Override
    @Transactional
    public OrderDto setOrderStatusToCancelled(Long orderId) {
        Order order = ordersDao.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
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

    private Order setOrder( ShoppingCart shoppingCart, User user, PaymentMethod paymentMethod) {
        Order order = new Order();
        List<OrderItem> orderItems = shoppingCart.getCartItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(shoppingCart.getTotal());
        order.setOrderDate(LocalDate.now());
        order.setUser(user);
        order.setOrderItems(orderItems);
        return order;
    }

    private void updateProductStock(ShoppingCart shoppingCart) {
        for (CartItem item : shoppingCart.getCartItems()) {
            productsService.updateProductStock(item.getProductId().getId(), item.getQuantity());
        }
    }

    private boolean validateOrder(ShoppingCart shoppingCart) {
        for (CartItem item : shoppingCart.getCartItems()) {
            Product product = productsDao.findById(item.getProductId().getId()).orElseThrow(() -> new ProductNotFoundException("Product not found"));
            if (product.getStock() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }
}
