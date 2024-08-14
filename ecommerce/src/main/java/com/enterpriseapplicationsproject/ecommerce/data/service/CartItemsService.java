package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.*;

import java.util.List;

public interface CartItemsService {

    List<CartItemDto> getCartItemsByCartId(Long cartId);

    CartItemDto save(CartItem cartitem);

    CartItemDto insert(InsertCartItemDto insertCartItemDto, Long bookId);

    boolean delete(Long id);

    CartItemDto updateQuantity(Long itemId, QuantityCartItemDto quantityCartItem);
}
