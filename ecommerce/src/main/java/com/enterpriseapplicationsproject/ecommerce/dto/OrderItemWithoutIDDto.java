package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

@Data
public class OrderItemWithoutIDDto {

    //private ProductDto product;

    private Long productId;

    private int quantity;

}
