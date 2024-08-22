package com.enterpriseapplicationsproject.ecommerce.dto;


import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class WishlistItemDto {

    private Long id;

    private Long wishlistId;
    /*
    private Long wishlistUserId;
    private Long wishlistGroupId;
    private String wishlistPrivacySetting;
    */
    private Book book;

    private int quantity;

}
