package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;

import java.util.List;

public interface WishlistItemsService {

    WishlistItemDto addItemToWishlist(WishlistItem wishlistItem);

    WishlistItemDto deleteItemById(WishlistItem wishlistItem);

    List<WishlistItemDto> getItemsByWishlistId(Long id);


    List<WishlistItemDto> getAllSorted();

    WishlistItemDto getById(Long id);
}
