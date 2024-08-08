package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsDao extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(ShoppingCart cartId);

    @Query("SELECT s FROM ShoppingCart s JOIN s.cartItems c WHERE c.id = :cartItemId")
    ShoppingCart findShoppingCartByCartItem(@Param("cartItemId") Long cartItemId);
}
