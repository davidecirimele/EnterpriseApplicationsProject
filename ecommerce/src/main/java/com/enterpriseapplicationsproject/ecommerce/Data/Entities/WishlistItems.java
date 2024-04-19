package com.enterpriseapplicationsproject.ecommerce.Data.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WishlistItems")
@Data
public class WishlistItems {

    @Id
    private long wishlistItemId;

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
