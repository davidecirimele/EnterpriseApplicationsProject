package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;

import java.util.List;

public interface WishlistItemsService {

    WishlistItemDto addItemToWishlist(Long wishlistId, WishlistItem wishlistItem);

    WishlistItemDto deleteByIdAndWishlistId(Long id, Long wishlistId);

    List<WishlistItemDto> getItemsByWishlistId(Long wishlistId);


    List<WishlistItemDto> getAllSorted();

    WishlistItemDto getById(Long id);
}
