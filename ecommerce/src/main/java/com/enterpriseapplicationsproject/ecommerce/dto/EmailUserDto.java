package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class EmailUserDto {

    private UUID id;

    @NotBlank(message = "Email is required")
    private String newEmail;

}
