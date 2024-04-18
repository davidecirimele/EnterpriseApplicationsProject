package com.enterpriseapplicationsproject.ecommerce.Data.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "CartItems")
@Data
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CART_ID", referencedColumnName = "ID")
    private ShoppingCart cartId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product productId;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "ADD_DATE")
    private LocalDate addDate;

    @Column(name = "PRICE")
    private Double price;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
