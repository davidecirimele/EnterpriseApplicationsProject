package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SavePaymentMethodDto;

import java.util.UUID;

public interface PaymentMethodsService {

    public SavePaymentMethodDto addPaymentMethod(SavePaymentMethodDto paymentMethodDto);

    public PaymentMethodDto getPaymentMethodByUserId(UUID userId);

    public PaymentMethodDto deletePaymentMethodByUserId(UUID userId);
}
