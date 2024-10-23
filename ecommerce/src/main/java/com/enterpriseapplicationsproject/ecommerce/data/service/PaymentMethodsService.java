package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SavePaymentMethodDto;

import java.util.List;
import java.util.UUID;

public interface PaymentMethodsService {

    public PaymentMethodDto addPaymentMethod(SavePaymentMethodDto paymentMethodDto);

    public List<PaymentMethodDto> getAllPaymentMethodByUserId(UUID userId);

    public PaymentMethodDto getPaymentMethodByUserId(UUID userId, Long paymentMethodId);

    public void deletePaymentMethodByUserId(UUID userId, Long paymentMethodId);
}
