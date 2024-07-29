package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Product;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.CartItemDto;
import com.enterpriseapplicationsproject.ecommerce.dto.CartItemIdDto;
import com.enterpriseapplicationsproject.ecommerce.dto.InsertCartItemDto;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;

import java.util.List;

public interface CartItemsService {

    //List<ProductDto> getProductByCartId(ShoppingCart cartId);

    CartItemDto save(CartItem cartitem);

    CartItemDto insert(InsertCartItemDto insertCartItemDto);

    boolean delete(CartItemIdDto id);
}
