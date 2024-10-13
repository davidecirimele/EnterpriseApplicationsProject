package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.data.service.ShoppingCartService;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartsDao shoppingCartDao;

    private final ModelMapper modelMapper;


    @Override
    public ShoppingCartDto getByUserId(UUID userId) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.findByUserId(userId);

        if(optionalShoppingCart.isPresent())
        {
            ShoppingCart cart = optionalShoppingCart.get();
            return modelMapper.map(cart, ShoppingCartDto.class);
        }
        else{
            throw new RuntimeException("Cart of user with id " + userId + " not found");
        }
    }

    @Override
    public ShoppingCartDto getByCartId(Long cartId) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.findById(cartId);

        if(optionalShoppingCart.isPresent())
        {
            ShoppingCart cart = optionalShoppingCart.get();
            return modelMapper.map(cart, ShoppingCartDto.class);
        }
        else{
            throw new RuntimeException("Cart with id " + cartId + " not found");
        }
    }

    @Override
    public ShoppingCartDto save(ShoppingCart sc) {
        ShoppingCart savedCart = shoppingCartDao.save(sc);
        return modelMapper.map(savedCart, ShoppingCartDto.class);
    }

    @Override
    public List<ShoppingCartDto> getAll() {
        List<ShoppingCart> shoppingCarts = shoppingCartDao.findAll();
        return shoppingCarts.stream().map(shoppingCart1 -> modelMapper.map(shoppingCart1 , ShoppingCartDto.class)).toList();
    }

    @Override
    public boolean delete(UUID userId, Long cartId){
        Optional<ShoppingCart> optionalSC = shoppingCartDao.findByUserId(userId);

        if(optionalSC.isPresent())
        {
            ShoppingCart shoppingCart = optionalSC.get();
            if(shoppingCart.getId().equals(cartId)) {
                shoppingCart.getCartItems().clear();

                shoppingCartDao.save(shoppingCart);
                return true;
            }
            else{
                throw new SecurityException("You can't access this resource");
            }
        }
        else{
            throw new RuntimeException("Cart with id " + cartId + " not found");
        }
    }

    @Override
    public ShoppingCartDto saveCart(ShoppingCartDto shoppingCartDto) {
        ShoppingCart cart = modelMapper.map(shoppingCartDto, ShoppingCart.class);
        ShoppingCart savedCart = shoppingCartDao.save(cart);
        return modelMapper.map(savedCart, ShoppingCartDto.class);
    }


}
