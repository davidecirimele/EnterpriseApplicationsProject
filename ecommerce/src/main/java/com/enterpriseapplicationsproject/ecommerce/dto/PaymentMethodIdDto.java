package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class PaymentMethodIdDto {

    @NotNull(message = "Payment method id cannot be null")
    private Long paymentMethodId;
}
