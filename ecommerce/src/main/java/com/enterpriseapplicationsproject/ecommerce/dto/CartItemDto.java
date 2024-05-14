package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CartItemDto {

    private Long id;

    private Long cartId;

    private Long productId;

    private Integer quantity;

    private LocalDate addDate;

    private Double price;
}
