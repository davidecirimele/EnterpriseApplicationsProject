package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.*;
import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.data.service.OrdersService;
import com.enterpriseapplicationsproject.ecommerce.data.service.TransactionsService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersDao ordersDao;

    private final ModelMapper modelMapper;

    private final UsersDao usersDao;

    private final PaymentMethodsDao paymentMethodsDao;


    private final ShoppingCartsDao shoppingCartDao;

    private final AddressesDao addressesDao;

    private final BooksService booksService;

    private final TransactionsDao transactionsDao;

    private final TransactionsService  transactionsService;

    private final BooksDao booksDao;


    @Override
    @Transactional
    public SaveOrderDto addOrder(CheckoutRequestDto orderDto) {
        User user = usersDao.findById(orderDto.getUserId().getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        ShoppingCart shoppingCart = shoppingCartDao.findByUserId(orderDto.getUserId().getUserId()).orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found"));
        Address add = addressesDao.findById(orderDto.getAddress().getId()).orElseThrow(() -> new AddressNotFoundException("Address not found"));


        if (!add.getUserId().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to this address");
        }
        PaymentMethod paymentMethod = paymentMethodsDao.findById(orderDto.getPaymentMethodId().getPaymentMethodId()).orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found"));

        if (!paymentMethod.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to this payment method");
        }
        if (!validateOrder(shoppingCart)) {
            throw new OutOfStockException("Products out of stock");
        }
        Order order = setOrder(shoppingCart, user, paymentMethod, add);

        try {
            Order savedOr = ordersDao.save(order);

            List<OrderItem> orderItems = getOrderItems(shoppingCart, savedOr);
            savedOr.setOrderItems(orderItems);

            Order fullOrder = ordersDao.save(savedOr);


            downProductStock(shoppingCart);
            Transaction transaction = transactionsService.addTransaction(user, savedOr, paymentMethod, savedOr.getTotalAmount(), PaymentStatus.APPROVED, LocalDate.now());
            transactionsDao.save(transaction);
            shoppingCart.getCartItems().clear();
            shoppingCartDao.save(shoppingCart);


            return modelMapper.map(fullOrder, SaveOrderDto.class);
        } catch (Exception e) {
            throw new OrderCreationException("Error creating order");
        }
    }

    @Override
    public List<OrderSummaryDto> getAllOrdersByUserId(UUID userId) {

        List<Order> orders = ordersDao.findAllByUserId(userId, Sort.by(Sort.Order.desc("orderDate")));


        return orders.stream().map(o -> modelMapper.map(o, OrderSummaryDto.class)).toList();
    }

    @Override
    public List<BookDto> getProductsByUserId(UUID userId) {

        List<Order> orders = ordersDao.findAllByUserId(userId, Sort.by(Sort.Order.desc("orderDate")));

        List<Book> purchased = new ArrayList<>();

        orders.forEach(order -> {
            order.getOrderItems().forEach(orderItem -> {
                if(!purchased.contains(orderItem.getBook())){
                    purchased.add(orderItem.getBook());
                }
            });
        });

        return purchased.stream().map(p -> modelMapper.map(p, BookDto.class)).toList();
    }

    @Override
    @Transactional
    public OrderDto setOrderStatusToCancelled(Long orderId, UUID userId) {

        Order order = ordersDao.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Unauthorized access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        Order o = ordersDao.save(order);
        return modelMapper.map(o, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllConfirmedOrdersByUserId(UUID userId) {
        List<Order> orders = ordersDao.findAllConfirmedOrdersByUserId(userId, Sort.by(Sort.Order.desc("orderDate")));
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }

    @Override
    public List<OrderDto> getAllCancelledOrdersByUserId(UUID userId) {
        List<Order> orders = ordersDao.findAllCancelledOrdersByUserId(userId, Sort.by(Sort.Order.desc("orderDate")));
        return orders.stream().map(o -> modelMapper.map(o, OrderDto.class)).toList();
    }

    @Override
    public Page<OrderSummaryDto> getAll(Pageable pageable) {
        Page<Order> orders = ordersDao.findAll(pageable);
        return orders.map(o -> modelMapper.map(o, OrderSummaryDto.class));
    }

    private Order setOrder( ShoppingCart shoppingCart, User user, PaymentMethod paymentMethod, Address address) {
        Order order = new Order();

        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(shoppingCart.getTotal());
        order.setOrderDate(LocalDate.now());
        order.setUser(user);
        order.setAddress(address);
        return order;
    }

    private void downProductStock(ShoppingCart shoppingCart) {
        for (CartItem item : shoppingCart.getCartItems()) {
            booksService.downBookStock(item.getBookId().getId(), item.getQuantity());
        }
    }

    private boolean validateOrder(ShoppingCart shoppingCart) {
        for (CartItem item : shoppingCart.getCartItems()) {
           Book book = booksDao.findById(item.getBookId().getId()).orElseThrow(() -> new BookNotFoundException("Book not found"));
            if (book.getStock() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    private List<OrderItem> getOrderItems(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(item.getBookId());
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());
    }
}
