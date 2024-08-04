package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Data;
import lombok.NonNull;

@Data
public class SaveOrderItemDto {

    private BookDto book;

    @Positive
    private Integer quantity;

}
