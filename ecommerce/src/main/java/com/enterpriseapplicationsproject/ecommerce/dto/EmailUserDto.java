package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailUserDto {

    private Long id;

    @NotBlank(message = "Email is required")
    private String newEmail;

}
