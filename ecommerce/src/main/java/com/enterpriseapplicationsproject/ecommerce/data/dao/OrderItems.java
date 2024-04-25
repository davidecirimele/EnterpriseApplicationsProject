package com.enterpriseapplicationsproject.ecommerce.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItems extends JpaRepository<OrderItems, Long> {
}