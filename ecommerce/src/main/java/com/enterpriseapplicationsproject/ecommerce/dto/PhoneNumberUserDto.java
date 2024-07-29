package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PhoneNumberUserDto {

    private Long id;

    @NotBlank(message = "Phone Number is Required")
    private String newPhoneNumber;
}
