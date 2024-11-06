package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveWishlistDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface WishlistsService {


    List<WishlistDto> getAllSorted();
    List<WishlistDto> getWishlistsByUser(UUID userId);

    List<WishlistDto> getFriendWishlists(UUID userId);


    List<Wishlist> getAll();


    WishlistDto save(SaveWishlistDto wishlistDto);

    WishlistDto save(WishlistDto wishlistDto);

    Group getGroupByWishlistId(Long wishlistId);

    WishlistDto updateWishlist(WishlistDto wishlistDto, UUID idUser);

    @Transactional
    Boolean JoinShareWishlist(UUID idUserToJoin, String wToken);

    @Transactional
    Boolean unshareWishlist(Long wishlistId, UUID idUser);

    WishlistDto getDtoById(Long id);

   // Wishlist getById(Long id);


    List<WishlistDto> getByLastname(String name);

    void deleteWishlist(Long id);
    WishlistDto deleteWishlistByID(Long id, UUID idUser);

    WishlistDto getWishlistByToken(String token);

    void save(Wishlist wishlist);

    WishlistDto save(UUID idUser, String wName, WishlistPrivacy wPrivacySetting);
}
