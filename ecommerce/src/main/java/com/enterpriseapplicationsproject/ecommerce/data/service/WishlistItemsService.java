package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;

import java.util.List;

public interface WishlistItemsService {

    WishlistItem addItemToWishlist(WishlistItem wishlistItem);

    Boolean deleteByIdAndWishlistId(Long id, Long wishlistId);

    List<WishlistItem> getItemsByWishlistId(Long wishlistId);




}
