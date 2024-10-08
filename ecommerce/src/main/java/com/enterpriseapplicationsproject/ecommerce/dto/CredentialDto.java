package com.enterpriseapplicationsproject.ecommerce.dto;


import com.enterpriseapplicationsproject.ecommerce.validation.ValidEmail;
import com.enterpriseapplicationsproject.ecommerce.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialDto {

    @NotBlank(message = "Email is required")
    @ValidEmail(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

}
