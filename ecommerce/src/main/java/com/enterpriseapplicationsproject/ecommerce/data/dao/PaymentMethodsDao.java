package com.enterpriseapplicationsproject.ecommerce.data.dao;



import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentMethodsDao extends JpaRepository<PaymentMethod, Long> {


    List<PaymentMethod> findAllByUserId(UUID userId);

    PaymentMethod findByUserIdAndPaymentMethodId(UUID userId, Long paymentMethodId);

    Integer deleteByUserIdAndPaymentMethodId(UUID userId, Long paymentMethodId);
}
