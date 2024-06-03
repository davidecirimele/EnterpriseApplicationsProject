package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(Long userId);

    ShoppingCartDto save(ShoppingCart sc);

}
