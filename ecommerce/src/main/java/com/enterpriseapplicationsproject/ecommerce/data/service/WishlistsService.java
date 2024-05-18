package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;

import java.util.List;

public interface WishlistsService {

    List<Wishlist> getWishlistsByUser(User user);

    List<Wishlist> getAll();

    Wishlist save(Wishlist wishlist);

    Group getGroupByWishlist(Wishlist wishlist);

    Boolean shareWishlist(Wishlist wishlist, List<User> users);

    Boolean unshareWishlist(Wishlist wishlist, List<User> users);
}
