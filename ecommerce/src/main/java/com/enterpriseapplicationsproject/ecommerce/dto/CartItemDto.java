package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CartItemDto {

    private Long id;

    private Long cartId;

    private BookCartDto bookId;

    private Integer quantity;

    private Double price;
}
