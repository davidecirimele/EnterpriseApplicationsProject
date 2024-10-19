package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserIdDto;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(UUID userId);

    ShoppingCartDto getByCartId(Long cartId);

    ShoppingCartDto save(ShoppingCart sc);

    List<ShoppingCartDto> getAll();

    boolean delete(UUID userId, Long cartId);

    ShoppingCartDto saveCart(ShoppingCartDto shoppingCartDto);

    Double getTotal(UUID userId);


}
