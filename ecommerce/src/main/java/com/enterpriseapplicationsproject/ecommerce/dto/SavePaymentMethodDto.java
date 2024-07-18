package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.validation.ValidExpirationYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SavePaymentMethodDto {
    private Long paymentMethodId;

    private UserIdDto user;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    private String  paymentMethodType;

    private String provider;

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiration date must be in the format MM/yy")
    @ValidExpirationYear
    private String expirationDate;

}

