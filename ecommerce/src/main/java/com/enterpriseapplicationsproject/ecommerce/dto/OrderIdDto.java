package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class OrderIdDto {
    @NotBlank(message = "The order id is required")
    private Long id;

}
