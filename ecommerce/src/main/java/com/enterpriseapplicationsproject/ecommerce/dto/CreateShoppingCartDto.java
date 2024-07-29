package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateShoppingCartDto {
    @NotNull(message = "User ID cannot be null")
    private UserIdDto userId;
}
