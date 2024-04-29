package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsDao extends JpaRepository<CartItem, Long> {
}
