package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository

public interface WishlistsDao extends JpaRepository<Wishlist, Long>,
        JpaSpecificationExecutor<Wishlist> {


    @Query("SELECT w FROM Wishlist w WHERE w.userId.id = :userId")
    List<Wishlist> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT w.group FROM Wishlist w WHERE w.id = :wishlistId")
    Group getGroupByWishlistId(@Param("wishlistId") Long wishlistId);

    @Query("SELECT w FROM Wishlist w " +
            "JOIN w.group g " +
            "JOIN g.members gm " +
            "WHERE gm.id = :userId AND w.userId.id != :userId")
    List<Wishlist> findOfFriends(@Param("userId") UUID userId);


}
