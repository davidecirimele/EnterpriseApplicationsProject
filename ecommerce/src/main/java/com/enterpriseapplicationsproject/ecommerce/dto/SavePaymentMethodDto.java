package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.domain.CardProvider;
import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentMethodType;
import com.enterpriseapplicationsproject.ecommerce.validation.ValidExpirationYear;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SavePaymentMethodDto {

    private UserIdDto user;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private CardProvider provider;

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiration date must be in the format MM/yy")
    @ValidExpirationYear
    private String expirationDate;

}

