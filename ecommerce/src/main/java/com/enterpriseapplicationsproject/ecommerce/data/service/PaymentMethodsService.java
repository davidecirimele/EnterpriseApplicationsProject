package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.PaymentMethodDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SavePaymentMethodDto;

public interface PaymentMethodsService {

    public SavePaymentMethodDto addPaymentMethod(SavePaymentMethodDto paymentMethodDto);

    public PaymentMethodDto getPaymentMethodByUserId(Long userId);

    public PaymentMethodDto deletePaymentMethodByUserId(Long userId);
}
