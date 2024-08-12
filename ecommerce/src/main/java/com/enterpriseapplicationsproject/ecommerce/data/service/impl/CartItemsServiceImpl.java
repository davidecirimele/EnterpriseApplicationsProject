package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.*;
import com.enterpriseapplicationsproject.ecommerce.data.dao.CartItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.CartItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemsService {

    private final ShoppingCartsDao shoppingCartsDao;
    private final CartItemsDao cartItemsDao;

    private final BooksDao booksDao;

    private final ModelMapper modelMapper;

    @Override
    public List<CartItemDto> getCartItemsByCartId(ShoppingCartIdDto cartId) {

        Optional<ShoppingCart> optionalCart = shoppingCartsDao.findByUserId(cartId.getUserId().getUserId());

        if(optionalCart.isPresent()){
            ShoppingCart cart = optionalCart.get();

            List<CartItem> cartItems = cartItemsDao.findByCartId(cart);

            return cartItems.stream().map(cartItem -> modelMapper.map(cartItem , CartItemDto.class)).toList();
        }
        else{
            throw new RuntimeException("No cart found with id "+cartId.getCartId());
        }
    }

    @Override
    public CartItemDto save(CartItem cartitem) {
        CartItem savedCartItem = cartItemsDao.save(cartitem);
        return modelMapper.map(savedCartItem, CartItemDto.class);
    }

    @Override
    public CartItemDto insert(InsertCartItemDto insertCartItemDto, Long bookId) {
        Optional<ShoppingCart> optionalCart = shoppingCartsDao.findByUserId(insertCartItemDto.getUserId().getUserId());
        Optional<Book> optionalBook = booksDao.findById(bookId);
        if(optionalCart.isPresent() && optionalBook.isPresent())
        {
            ShoppingCart cart = optionalCart.get();
            Book book = optionalBook.get();

            for(CartItem c : cart.getCartItems())
            {
                if(c.getBookId().getId().equals(book.getId())){
                    c.setQuantity(c.getQuantity()+insertCartItemDto.getQuantity());
                    cartItemsDao.save(c);
                    return modelMapper.map(c, CartItemDto.class);
                }
            }

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
    public boolean delete(Long id) {
        Optional<CartItem> optionalCartItem = cartItemsDao.findById(id);

        if(optionalCartItem.isPresent())
        {
            cartItemsDao.delete(optionalCartItem.get());
            return true;
        }
        else{
            throw new RuntimeException("Item with id " + id + " not found");
        }
    }

    @Override
    public CartItemDto updateQuantity(Long id, QuantityCartItemDto quantityCartItem){
        Optional<CartItem> optionalCartItem = cartItemsDao.findById(id);

        if(optionalCartItem.isPresent())
        {
            CartItem cartItem = optionalCartItem.get();

            if(quantityCartItem.getQuantity() > 0)
            {
                cartItem.setQuantity(quantityCartItem.getQuantity());
                cartItemsDao.save(cartItem);
                return modelMapper.map(cartItem,CartItemDto.class);
            }
            else{
                cartItemsDao.delete(cartItem);
                return null;
            }

        }
        else{
            throw new RuntimeException("Item with id " + id + " not found");
        }
    }
}
