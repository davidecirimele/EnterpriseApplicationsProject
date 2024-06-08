package com.enterpriseapplicationsproject.ecommerce.data.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "WishlistItems")
@Data
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "WISHLIST_ID",
            referencedColumnName = "ID"
    )
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(
            name = "PRODUCT_ID",
            referencedColumnName = "ID"
    )
    private Product product;

}