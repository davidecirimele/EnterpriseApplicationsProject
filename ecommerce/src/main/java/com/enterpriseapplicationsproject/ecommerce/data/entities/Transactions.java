package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.enterpriseapplicationsproject.ecommerce.data.domain.Status;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @Column(name = "USER_ID", nullable = false)
    private Users userId;

    @Column(name = "ORDER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Orders orderId;

    @Column(name = "PAYMENT_METHOD_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_METHOD_ID", nullable = false)
    private PaymentMethods paymentMethodId;

    @Column(name = "AMOUNT")
    private double amount;

    @Column(name = "PAYMENT_STATUS")
    @Enumerated(EnumType.STRING)
    private Status paymentStatus;

    @Column(name = "DATE")
    private LocalDate date;










}
