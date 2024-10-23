package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PriceDto {

    @NotBlank(message = "You must specify a price")
    @Positive
    private Double price;

}
