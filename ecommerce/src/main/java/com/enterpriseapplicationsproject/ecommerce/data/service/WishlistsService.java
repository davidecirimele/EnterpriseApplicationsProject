package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;

import java.util.List;

public interface WishlistsService {


    List<WishlistDto> getAllSorted();
    List<Wishlist> getWishlistsByUser(User user);

    List<Wishlist> getAll();

    Wishlist save(Wishlist wishlist);

    Group getGroupByWishlist(Wishlist wishlist);

    Boolean shareWishlist(Wishlist wishlist, Group group);

    Boolean unshareWishlist(Wishlist wishlist,Group group);

    WishlistDto getById(Long id);

    List<WishlistDto> getByLastname(String name);
}
