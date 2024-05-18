package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistsDao extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserId(Long userId);

    Group getGroupByWishlist(Wishlist wishlist);

    Boolean shareWishlist(Wishlist wishlist, Group group);

    Boolean unshareWishlist(Wishlist wishlist, Group group);


}
