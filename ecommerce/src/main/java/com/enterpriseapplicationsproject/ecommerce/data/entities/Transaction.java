package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @JoinColumn(name = "PAYMENT_METHOD_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethod paymentMethod;


    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "PAYMENT_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

}
