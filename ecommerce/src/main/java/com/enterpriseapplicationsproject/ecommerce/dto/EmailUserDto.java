package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.validation.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailUserDto {

    @NotBlank(message = "Email is required")
    @ValidEmail(message = "Email should be valid")
    private String newEmail;

}
