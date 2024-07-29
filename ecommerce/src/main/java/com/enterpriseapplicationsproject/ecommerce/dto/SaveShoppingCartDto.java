package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SaveShoppingCartDto {
    @NotBlank(message = "Cart ID cannot be null")
    private Long cartId;
    @NotBlank(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Cart items cannot be null")
    private List<CartItemDto> items;
}
