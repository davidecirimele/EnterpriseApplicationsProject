package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class EmailUserDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String newEmail;

}
