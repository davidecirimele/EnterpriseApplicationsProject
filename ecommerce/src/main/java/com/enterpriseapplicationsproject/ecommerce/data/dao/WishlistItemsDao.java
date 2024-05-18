package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemsDao extends JpaRepository<WishlistItem, Long> {

    Boolean deleteByIdAndWishlistId(Long id, Long wishlistId);

    List<WishlistItem> findByWishlistId(Long wishlistId);

}
