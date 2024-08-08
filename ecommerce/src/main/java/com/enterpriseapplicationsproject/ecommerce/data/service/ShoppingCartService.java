package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserIdDto;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(UUID userId);

    ShoppingCartDto save(ShoppingCart sc);

    List<ShoppingCartDto> getAll();

    boolean delete(UserIdDto id);

    ShoppingCartDto saveCart(ShoppingCartDto shoppingCartDto);
}
