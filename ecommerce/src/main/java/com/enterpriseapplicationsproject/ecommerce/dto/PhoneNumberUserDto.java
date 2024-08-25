package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class PhoneNumberUserDto {

    @NotBlank(message = "Phone Number is Required")
    @ValidPhoneNumber
    private String newPhoneNumber;

}
