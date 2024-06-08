package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface WishlistsService {


    List<WishlistDto> getAllSorted();
    List<WishlistDto> getWishlistsByUser(Long userId);

    List<Wishlist> getAll();

    void save(Wishlist wishlist);

    WishlistDto save(WishlistDto wishlistDto);

    Group getGroupByWishlistId(Long wishlistId);

    WishlistDto updateWishlist(Long id, WishlistDto wishlistDto);

    @Transactional
    Boolean shareWishlist(Long wishlistId, Group group);

    @Transactional
    Boolean unshareWishlist(Long wishlistId);

    WishlistDto getById(Long id);

    List<WishlistDto> getByLastname(String name);

    void deleteWishlist(Long id);
    WishlistDto deleteWishlistByID(Long id);
}
