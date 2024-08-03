package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.CartItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ProductsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.CartItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemsService {

    private final ShoppingCartsDao shoppingCartsDao;
    private final CartItemsDao cartItemsDao;

    private final ProductsDao productsDao;

    private final ModelMapper modelMapper;

    //@Override
    //public List<ProductDto> getProductByCartId(ShoppingCart cartId) {
     //   List<Product> products = cartItemsDao.findProductsbyCartId(cartId);
     //   return products.stream().map(product -> modelMapper.map(product , ProductDto.class)).toList();
    //}

    @Override
    public CartItemDto save(CartItem cartitem) {
        CartItem savedCartItem = cartItemsDao.save(cartitem);
        return modelMapper.map(savedCartItem, CartItemDto.class);
    }

    @Override
    public CartItemDto insert(InsertCartItemDto insertCartItemDto) {
        Optional<ShoppingCart> optionalCart = shoppingCartsDao.findByUserId(insertCartItemDto.getUserId().getUserId());
        Optional<Product> optionalProduct = productsDao.findById(insertCartItemDto.getProductId().getId());
        if(optionalCart.isPresent() && optionalProduct.isPresent())
        {
            ShoppingCart cart = optionalCart.get();
            Product product = optionalProduct.get();

            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart);
            cartItem.setProductId(product);
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(insertCartItemDto.getQuantity());
            cartItem.setAddDate(LocalDateTime.now());

            CartItem insertedItem = cartItemsDao.save(cartItem);
            return modelMapper.map(insertedItem, CartItemDto.class);
        }

        return null;
    }

    @Override
    public boolean delete(CartItemIdDto id) {
        Optional<CartItem> optionalCartItem = cartItemsDao.findById(id.getId());

        if(optionalCartItem.isPresent())
        {
            cartItemsDao.delete(optionalCartItem.get());
            return true;
        }
        else{
            throw new RuntimeException("Item with id " + id.getId() + " not found");
        }
    }
}