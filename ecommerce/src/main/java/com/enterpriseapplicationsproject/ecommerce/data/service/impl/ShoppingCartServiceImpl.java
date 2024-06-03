package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.ShoppingCartService;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartsDao shoppingCartDao;

    private final ModelMapper modelMapper;

    @Override
    public ShoppingCartDto getByUserId(Long cartId) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.findById(cartId);

        if(optionalShoppingCart.isPresent())
        {
            ShoppingCart cart = optionalShoppingCart.get();
            return modelMapper.map(cart, ShoppingCartDto.class);
        }
        else{
            throw new RuntimeException("User with id " + cartId + " not found");
        }
    }

    @Override
    public ShoppingCartDto save(ShoppingCart sc) {
        ShoppingCart savedCart = shoppingCartDao.save(sc);
        return modelMapper.map(savedCart, ShoppingCartDto.class);
    }
}
