package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import com.enterpriseapplicationsproject.ecommerce.data.domain.Status;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Order_table")
public class Order {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", nullable = false)
    private Address address;

    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    @Column(name = "TOTAL_AMOUNT")
    private double totalAmount;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_METHOD_ID", nullable = false)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;


}
