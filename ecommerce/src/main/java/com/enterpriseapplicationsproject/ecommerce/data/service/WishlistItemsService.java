package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;

import java.util.List;

public interface WishlistItemsService {

    WishlistItem addItemToWishlist(Long wishlistId, WishlistItem wishlistItem);

    Boolean deleteByIdAndWishlistId(Long id, Long wishlistId);

    List<WishlistItem> getItemsByWishlistId(Long wishlistId);


    List<WishlistItemDto> getAllSorted();

    WishlistItem getById(Long id);
}
