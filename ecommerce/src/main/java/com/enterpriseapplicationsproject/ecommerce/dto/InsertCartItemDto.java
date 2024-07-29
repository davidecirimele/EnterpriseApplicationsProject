package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InsertCartItemDto {

    @NotBlank
    private Long cartId;

    @NotNull
    private UserIdDto userId;

    @NotBlank
    private ProductIdDto productId;

    @NotBlank
    private Integer quantity;

    @NotBlank
    private LocalDate addDate;

}
