package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemsDao extends JpaRepository<WishlistItem, Long> {


    @Query("SELECT w FROM WishlistItem w WHERE w.wishlist.id = :wishlistId")
    List<WishlistItemDto> findByWishlistId(Long wishlistId);

    @Query("DELETE FROM WishlistItem w WHERE w.id = :id AND w.wishlist.id = :wishlistId")
    WishlistItemDto deleteByIdAndWishlistId(Long id, Long wishlistId);



    @Query("SELECT w FROM WishlistItem w WHERE w.id = :id")
    WishlistItemDto findByIdDto(Long id);

}
