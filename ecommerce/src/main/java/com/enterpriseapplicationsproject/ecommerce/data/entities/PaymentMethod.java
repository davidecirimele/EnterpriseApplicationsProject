package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long paymentMethodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "Card Holder Name", nullable = false)
    private String cardHolderName;

    @Column(name = "TYPE", nullable = false)
    private String paymentMethodType;

    @Column(name = "PROVIDER", nullable = false)
    private String provider;

    @Column(name = "CARD_NUMBER", nullable = false)
    private String cardNumber;

    @Column(name = "EXPIRY_DATE", nullable = false)
    private String expirationDate;

    @Column(name = "paypal")
    private String paypalEmail;
}
