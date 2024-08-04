package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.*;
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

    private final BooksDao booksDao;

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
        Optional<Book> optionalBook = booksDao.findById(insertCartItemDto.getBookId().getId());
        if(optionalCart.isPresent() && optionalBook.isPresent())
        {
            ShoppingCart cart = optionalCart.get();
            Book book = optionalBook.get();

            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart);
            cartItem.setBookId(book);
            cartItem.setPrice(book.getPrice());
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
