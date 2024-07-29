package com.enterpriseapplicationsproject.ecommerce.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialDto {

    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

}
