package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;

import java.util.List;
import java.util.UUID;

public interface WishlistItemsService {

    WishlistItemDto addItemToWishlist(WishlistItem wishlistItem);

    WishlistItemDto deleteItemById(Long idWishlistItem, UUID userId);

    List<WishlistItemDto> getItemsByWishlistId(Long id, UUID idUser);


    List<WishlistItemDto> getAllSorted();

    WishlistItemDto getById(Long id);

    void save(WishlistItem wishlistItem);
}
