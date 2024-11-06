package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.CartItem;
import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.data.service.ShoppingCartService;
import com.enterpriseapplicationsproject.ecommerce.dto.ShoppingCartDto;
import com.enterpriseapplicationsproject.ecommerce.exception.ShoppingCartNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartsDao shoppingCartDao;

    private final ModelMapper modelMapper;


    @Override
    public ShoppingCartDto getByUserId(UUID userId) {
        try {
            ShoppingCart cart = shoppingCartDao.findByUserId(userId).orElseThrow(()->new ShoppingCartNotFoundException("Shopping cart not found"));

            return modelMapper.map(cart, ShoppingCartDto.class);
            } catch(Exception e) {
            log.error("Unexpected error while fetching shopping cart for user with ID: "+userId+", " + e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public ShoppingCartDto save(ShoppingCart sc) {
        try {
            ShoppingCart savedCart = shoppingCartDao.save(sc);
            return modelMapper.map(savedCart, ShoppingCartDto.class);
        }catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while saving shopping cart: {}", sc, e);
            throw new IllegalArgumentException("Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while saving shopping cart: {}", sc, e);
            throw new RuntimeException("Unexpected error occurred while saving shopping cart");
        }
    }

    @Override
    public List<ShoppingCartDto> getAll() {
        try {
            List<ShoppingCart> shoppingCarts = shoppingCartDao.findAll();
            return shoppingCarts.stream().map(shoppingCart1 -> modelMapper.map(shoppingCart1, ShoppingCartDto.class)).toList();
        }catch (Exception e) {
            log.error("Unexpected error while fetching shopping carts: " + e);
            throw new RuntimeException("Unexpected error occurred while fetching shopping carts");
        }
    }

    @Override
    public boolean delete(UUID userId, Long cartId){
        try {
            ShoppingCart shoppingCart = shoppingCartDao.findByUserId(userId).orElseThrow(()->new ShoppingCartNotFoundException("Shopping cart not found"));


            if (shoppingCart.getId().equals(cartId)) {
                shoppingCart.getCartItems().clear();

                shoppingCartDao.save(shoppingCart);
                return true;
            } else {
                throw new SecurityException("You can't access this resource");
            }
        }catch(Exception e){
            log.error("Unexpected error while deleting shopping cart with ID: "+ cartId +", " + e);
            throw new RuntimeException("Unexpected error occurred while deleting shopping cart");
        }
    }

    @Override
    public Double getTotal(UUID userId) {
        try {
            ShoppingCart cart = shoppingCartDao.findByUserId(userId).orElseThrow(()->new ShoppingCartNotFoundException("Shopping cart not found"));

            return cart.getTotal();
        }catch(Exception e){
            log.error("Unexpected error while fetching shopping cart total for user with ID: "+ userId +", " + e);
            throw new RuntimeException("Unexpected error occurred while fetching shopping cart total");
        }
    }
}
