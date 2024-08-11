package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ShoppingCartIdDto {

    @NotBlank
    private UserIdDto userId;

    @NotBlank
    private Long cartId;

}
