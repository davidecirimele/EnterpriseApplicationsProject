package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderItemDto {

    private Long orderItemId;

    private BookDto book;

    private int quantity;
}
