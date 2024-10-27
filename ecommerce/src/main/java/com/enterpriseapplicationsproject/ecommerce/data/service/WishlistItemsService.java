package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;

import java.util.List;
import java.util.UUID;

public interface WishlistItemsService {

    WishlistItemDto addItem(WishlistItem wishlistItem);

    WishlistItemDto addItem(Long idBook, Long idWishlist, UUID idUser);


    WishlistItemDto deleteItemById(Long idWishlistItem, UUID userId);

    List<WishlistItemDto> getItemsByWishlistId(Long id, UUID idUser);


    List<WishlistItemDto> getAllSorted();

    WishlistItemDto getById(Long id);

    void save(WishlistItem wishlistItem);

}
