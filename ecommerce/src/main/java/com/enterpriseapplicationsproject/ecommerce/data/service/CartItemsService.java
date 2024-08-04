package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Product;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.*;

import java.util.List;

public interface CartItemsService {

    //List<ProductDto> getProductByCartId(ShoppingCart cartId);

    CartItemDto save(CartItem cartitem);

    CartItemDto insert(InsertCartItemDto insertCartItemDto);

    boolean delete(CartItemIdDto id);

    CartItemDto updateQuantity(QuantityCartItemDto quantityCartItem);
}
