package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUserDto {

    private Long id;
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    public Long getId() {
        return id;
    }

    public String getOldPassword(){
        return oldPassword;
    }

    public String getNewPassword(){
        return newPassword;
    }
}

