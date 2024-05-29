package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface WishlistsDao extends JpaRepository<Wishlist, Long>,
        JpaSpecificationExecutor<Wishlist> {

    List<Wishlist> findByUserId(Long userId);

    Group getGroupByWishlist(Wishlist wishlist);

    Boolean shareWishlist(Wishlist wishlist, Group group);

    Boolean unshareWishlist(Wishlist wishlist, Group group);


}
