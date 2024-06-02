package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;

public interface PaymentMethodsService {

    public PaymentMethodDto addPaymentMethod(PaymentMethodDto paymentMethodDto);

    public PaymentMethodDto getPaymentMethodByUserId(Long userId);

    public PaymentMethodDto deletePaymentMethodByUserId(Long userId);
}
