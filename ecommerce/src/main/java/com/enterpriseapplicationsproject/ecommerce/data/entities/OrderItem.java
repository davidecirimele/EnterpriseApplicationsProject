package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint( columnNames = {"ORDER_ID", "BOOK_ID"}), name = "ORDER_ITEMS")

public class OrderItem {

        @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

}
