package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersDao  extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(UUID userId, Sort sort);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus = 'CANCELLED'")
    List<Order> findAllCancelledOrdersByUserId(UUID userId, Sort sort);

    List<Order> findAllConfirmedOrdersByUserId(UUID userId, Sort sort);


}
