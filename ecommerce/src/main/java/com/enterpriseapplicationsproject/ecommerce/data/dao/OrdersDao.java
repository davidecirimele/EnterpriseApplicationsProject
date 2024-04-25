package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDao  extends JpaRepository<Order, Long> {
}
