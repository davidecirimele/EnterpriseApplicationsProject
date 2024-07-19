package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderItemDto {

    private Long orderItemId;

    private ProductDto product;

    private int quantity;
}
