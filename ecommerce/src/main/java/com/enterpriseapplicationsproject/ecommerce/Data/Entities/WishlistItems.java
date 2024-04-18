package com.enterpriseapplicationsproject.ecommerce.Data.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class WishlistItems {

    @Id
    private long wishlistItemId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "wishlistId")
    private List<Wishlist> wishlistId = new ArrayList<>();

    @ManyToOne
    private long productId;

}
