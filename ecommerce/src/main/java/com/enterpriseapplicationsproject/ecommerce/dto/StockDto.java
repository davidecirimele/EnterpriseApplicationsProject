package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockDto {

    @NotBlank(message = "You must specify a stock number")
    @Positive
    private Integer stock;

}
