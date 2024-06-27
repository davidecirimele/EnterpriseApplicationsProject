package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Product;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class WishlistItemDto {

    private Long id;

    private Wishlist wishlist;

    private Product product;
}
