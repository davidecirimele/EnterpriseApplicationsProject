package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ShoppingCartDto {

    private Long cartId;

    private Long userId;

    private List<CartItem> cartItems;

}
