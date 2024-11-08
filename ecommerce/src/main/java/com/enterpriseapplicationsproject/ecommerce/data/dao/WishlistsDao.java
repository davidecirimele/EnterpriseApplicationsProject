package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistTokenDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy.*;

import java.util.List;
import java.util.UUID;

@Repository

public interface WishlistsDao extends JpaRepository<Wishlist, Long>,
        JpaSpecificationExecutor<Wishlist> {


    @Query("SELECT w FROM Wishlist w WHERE w.userId.id = :userId")
    List<Wishlist> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT w.group FROM Wishlist w WHERE w.id = :wishlistId")
    Group getGroupByWishlistId(@Param("wishlistId") Long wishlistId);

    //@Query("SELECT distinct w FROM Wishlist w JOIN w.group.members m WHERE m.id = :userId and w.privacySetting <> 0  and w.userId.id <> :userId")
    @Query("SELECT DISTINCT w FROM Wishlist w JOIN w.group g JOIN g.members m WHERE m.id = :userId AND w.privacySetting <> com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy.PRIVATE")
    List<Wishlist> findFriendWishlists(@Param("userId")UUID userId);

    Wishlist findWishlistByGroup_Id(Long groupId);
    /*
    @Query("SELECT w FROM Wishlist w WHERE w.wishlistToken = :token")
    Wishlist findByToken(@Param("token") String token);
    */

    Wishlist findWishlistByWishlistToken(String wishlistToken);
}
