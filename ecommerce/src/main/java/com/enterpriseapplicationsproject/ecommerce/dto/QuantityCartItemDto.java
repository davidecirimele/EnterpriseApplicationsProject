package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuantityCartItemDto {

    @NotBlank(message = "User id cannot be blank")
    private UserIdDto userId;

    @NotBlank(message = "You must specify a quantity")
    @Positive
    private Integer quantity;

}
