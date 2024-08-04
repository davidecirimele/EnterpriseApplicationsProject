package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import com.enterpriseapplicationsproject.ecommerce.dto.CartItemDto;
import com.enterpriseapplicationsproject.ecommerce.dto.CartItemIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.InsertCartItemDto;

public interface CartItemsService {

    //List<ProductDto> getProductByCartId(ShoppingCart cartId);

    CartItemDto save(CartItem cartitem);

    CartItemDto insert(InsertCartItemDto insertCartItemDto);

    boolean delete(CartItemIdDto id);
}
