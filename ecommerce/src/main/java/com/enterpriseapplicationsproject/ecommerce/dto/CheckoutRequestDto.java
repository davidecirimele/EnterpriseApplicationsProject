package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CheckoutRequestDto {

    @NotNull(message = "User id cannot be null")
    @Valid
    UserIdDto userId;
    @NotNull(message = "Address cannot be null")
    @Valid
    AddressDto address;

    @NotNull(message = "Payment method id cannot be null")
    @Valid
    PaymentMethodIdDto paymentMethodId;
}
