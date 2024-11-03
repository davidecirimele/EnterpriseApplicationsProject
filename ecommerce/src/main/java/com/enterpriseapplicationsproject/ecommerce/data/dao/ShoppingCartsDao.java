package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingCartsDao extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT s FROM ShoppingCart s WHERE s.userId.id =:userId")
    Optional<ShoppingCart> findByUserId(@Param("userId") UUID userId);
}