package com.enterpriseapplicationsproject.ecommerce.data.dao;



import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentMethodsDao extends JpaRepository<PaymentMethod, Long> {


    PaymentMethod findByUserId(UUID userId);

    PaymentMethod deleteByUserId(UUID userId);
}
