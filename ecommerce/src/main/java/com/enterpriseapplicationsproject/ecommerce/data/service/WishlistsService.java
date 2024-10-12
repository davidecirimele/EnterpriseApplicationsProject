package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface WishlistsService {


    List<WishlistDto> getAllSorted();
    List<WishlistDto> getWishlistsByUser(UUID userId);

    List<WishlistDto> getFriendWishlists(UUID userId);


    List<Wishlist> getAll();

    void save(Wishlist wishlist);

    WishlistDto save(WishlistDto wishlistDto);

    Group getGroupByWishlistId(Long wishlistId);

    WishlistDto updateWishlist(WishlistDto wishlistDto);

    @Transactional
    Boolean JoinShareWishlist(UUID idUserToJoin, String wToken);

    @Transactional
    Boolean unshareWishlist(Long wishlistId);

    WishlistDto getDtoById(Long id);

   // Wishlist getById(Long id);


    List<WishlistDto> getByLastname(String name);

    void deleteWishlist(Long id);
    WishlistDto deleteWishlistByID(Long id);

    WishlistDto getWishlistByToken(String token);
}
