package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.CreateShoppingCartDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveShoppingCartDto;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserIdDto;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(Long userId);

    ShoppingCartDto save(ShoppingCart sc);

    List<ShoppingCartDto> getAll();

    boolean delete(UserIdDto id);

    ShoppingCartDto createCart(CreateShoppingCartDto createShoppingCartDto);
    ShoppingCartDto saveCart(SaveShoppingCartDto saveShoppingCartDto);
}
