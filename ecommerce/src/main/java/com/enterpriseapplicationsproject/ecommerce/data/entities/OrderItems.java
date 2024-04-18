package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint( columnNames = {"ORDER_ID", "PRODUCT_ID"}))

public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Products product;

    @Column(name = "QUANTITY")
    private Integer quantity;

}
