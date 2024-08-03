package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

@Data
public class QuantityCartItemDto {

    private Long id;

    private UserIdDto userId;

    private Integer quantity;

}
