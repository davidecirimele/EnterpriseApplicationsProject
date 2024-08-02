package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import lombok.Data;

@Data
public class CartItemIdDto {

    private Long id;

    private UserIdDto userId;

}
