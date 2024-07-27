package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

@Data
public class CheckoutRequestDto {
    UserIdDto userId;
    AddressDto address;
    PaymentMethodIdDto paymentMethodId;
}
