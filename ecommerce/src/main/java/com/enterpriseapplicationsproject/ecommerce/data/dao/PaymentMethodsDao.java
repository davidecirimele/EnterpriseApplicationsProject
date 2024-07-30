package com.enterpriseapplicationsproject.ecommerce.data.dao;



import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import com.enterpriseapplicationsproject.ecommerce.data.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodsDao extends JpaRepository<PaymentMethod, Long> {


    PaymentMethod findByUserId(Long userId);

    PaymentMethod deleteByUserId(Long userId);

    void updatePaymentMethodStatus(Long paymentMethodId, PaymentStatus status);
}
