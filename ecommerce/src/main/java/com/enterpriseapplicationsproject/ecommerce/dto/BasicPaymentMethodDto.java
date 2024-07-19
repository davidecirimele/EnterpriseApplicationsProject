package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BasicPaymentMethodDto {
    private String paymentMethodType;

    private String provider;

    private String cardNumber;
}
