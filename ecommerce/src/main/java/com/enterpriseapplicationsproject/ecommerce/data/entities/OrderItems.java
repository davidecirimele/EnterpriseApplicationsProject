package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Orders orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Products productId;

    @Column(name = "QUANTITY")
    private int quantity;

}
