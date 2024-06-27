package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersDao  extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus = 'CANCELLED'")
    List<Order> findAllCancelledOrdersByUserId(Long userId);

    List<Order> findAllConfirmedOrdersByUserId(Long userId);
}
