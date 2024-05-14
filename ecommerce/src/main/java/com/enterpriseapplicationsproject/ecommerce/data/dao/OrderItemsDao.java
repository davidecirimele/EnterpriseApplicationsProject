package com.enterpriseapplicationsproject.ecommerce.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsDao extends JpaRepository<OrderItem, Long> {
}
