package com.enterpriseapplicationsproject.ecommerce.dto;



import com.enterpriseapplicationsproject.ecommerce.validation.ValidExpirationYear;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDate;

@Data
public class PaymentMethodDto {

    private Long paymentMethodId;

    private UserIdDto user;

    private String cardHolderName;

    private String  paymentMethodType;

    private String provider;

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiration date must be in the format MM/yy")
    @ValidExpirationYear
    private String expirationDate;

    private  String paypalEmail;
}
