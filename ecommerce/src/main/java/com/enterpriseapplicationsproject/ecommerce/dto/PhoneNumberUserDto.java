package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class PhoneNumberUserDto {

    @NotBlank(message = "Phone Number is Required")
    private String newPhoneNumber;
}
