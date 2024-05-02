package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsDao extends JpaRepository<OrderItem, Long> {
}