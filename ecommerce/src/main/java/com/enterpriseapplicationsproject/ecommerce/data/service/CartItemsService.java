package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.*;

import java.util.List;
import java.util.UUID;

public interface CartItemsService {

    List<CartItemDto> getCartItems(UUID userId, Long cartId);

    CartItemDto save(CartItem cartitem);

    CartItemDto insert(QuantityCartItemDto quantityCartItemDto, UUID userId, Long bookId, Long cartId);

    boolean delete(UUID userId, Long cartId, Long id);

    CartItemDto updateQuantity(UUID userId, Long cartId, Long id, QuantityCartItemDto quantityCartItem);
}
