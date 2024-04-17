package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @Column(name = "USER_ID")
    private Users userId;

    @Column(name = "ORDER_ID")
    private Orders orderId;

    @Column(name = "PAYMENTMETHOD_ID")
    private PaymentMethods paymentMethodId;

    @Column(name = "AMOUNT")
    private double amount;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "DATE")
    private LocalDate date;










}
