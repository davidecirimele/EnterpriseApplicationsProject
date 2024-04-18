package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import com.enterpriseapplicationsproject.ecommerce.data.domain.Status;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Orders {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Users userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", nullable = false)
    private Addresses addressId;

    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    @Column(name = "TOTAL_AMOUNT")
    private double totalAmount;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_METHOD_ID", nullable = false)
    private PaymentMethods paymentMethodId;

    @OneToMany(mappedBy = "orderItemId")
    private List<OrderItems> orderItems;


}
