package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class PaymentMethods {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long paymentMethodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Users user;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "PROVIDER")
    private String provider;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @Column(name = "paypal")
    private String paypal;

    @OneToMany(mappedBy =  "transaction")
    private List<Transactions> transactions;

    @OneToMany(mappedBy = "order")
    private List<Orders> orders;














}
