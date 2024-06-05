package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long paymentMethodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "PROVIDER", nullable = false)
    private String provider;

    @Column(name = "CARD_NUMBER", nullable = false)
    private String cardNumber;

    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "paypal")
    private String paypal;

   /* @OneToMany(mappedBy =  "paymentMethod")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Order> orders;


    */

}
