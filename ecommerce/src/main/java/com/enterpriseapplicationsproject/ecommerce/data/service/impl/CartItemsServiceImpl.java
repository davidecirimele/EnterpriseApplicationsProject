package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.*;
import com.enterpriseapplicationsproject.ecommerce.data.dao.CartItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.CartItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemsService {

    private final ShoppingCartsDao shoppingCartsDao;
    private final CartItemsDao cartItemsDao;

    private final BooksDao booksDao;

    private final ModelMapper modelMapper;

    @Override
    public List<CartItemDto> getCartItems(UUID userId, Long cartId) {

        Optional<ShoppingCart> optionalCart = shoppingCartsDao.findByUserId(userId);

        if(optionalCart.isPresent()){
            ShoppingCart cart = optionalCart.get();

            if(cart.getId().equals(cartId)) {
                List<CartItem> cartItems = cartItemsDao.findByCartId(cart);
                return cartItems.stream().map(cartItem -> modelMapper.map(cartItem, CartItemDto.class)).toList();
            }
            else{
                throw new SecurityException("You can't access this resource");
            }
        }
        else{
            throw new RuntimeException("No cart found with id "+cartId);
        }
    }

    @Override
    public CartItemDto save(CartItem cartitem) {
        CartItem savedCartItem = cartItemsDao.save(cartitem);
        return modelMapper.map(savedCartItem, CartItemDto.class);
    }

    @Override
    public CartItemDto insert(QuantityCartItemDto insertCartItemDto, UUID userId, Long cartId, Long bookId) {
        Optional<ShoppingCart> optionalCart = shoppingCartsDao.findByUserId(userId);
        Optional<Book> optionalBook = booksDao.findById(bookId);
        if(optionalCart.isPresent() && optionalBook.isPresent())
        {
            ShoppingCart cart = optionalCart.get();

            if(cart.getId().equals(cartId)) {
                Book book = optionalBook.get();

                for (CartItem c : cart.getCartItems()) {
                    if (c.getBookId().getId().equals(book.getId())) {
                        c.setQuantity(c.getQuantity() + insertCartItemDto.getQuantity());
                        cartItemsDao.save(c);
                        return modelMapper.map(c, CartItemDto.class);
                    }
                }

                CartItem cartItem = new CartItem();
                cartItem.setCartId(cart);
                cartItem.setBookId(book);
                cartItem.setPrice(book.getPrice());
                cartItem.setQuantity(insertCartItemDto.getQuantity());

                CartItem insertedItem = cartItemsDao.save(cartItem);
                return modelMapper.map(insertedItem, CartItemDto.class);
            }
            else{
                throw new SecurityException("You can't access this resource");
            }
        }
        else {
            throw new RuntimeException("Error inserting item in Shopping Cart");
        }
    }

    @Override
    public boolean delete(UUID userId, Long cartId, Long itemId) {
        Optional<ShoppingCart> optionalCart = shoppingCartsDao.findByUserId(userId);
        Optional<CartItem> optionalCartItem = cartItemsDao.findById(itemId);

        if(optionalCart.isPresent() && optionalCartItem.isPresent())
        {
            ShoppingCart shoppingCart = optionalCart.get();
            CartItem cartItem = optionalCartItem.get();

            log.info("found shoppingCart: "+shoppingCart.getId()+", passed cartID: "+cartId+", passed itemId: "+itemId+"foundItemCartID: "+cartItem.getCartId());
            if (shoppingCart.getId().equals(cartId) && cartItem.getCartId().getId().equals(cartId)) {
                cartItemsDao.delete(optionalCartItem.get());
                return true;
            }
            else{
                throw new SecurityException(("You can't access this resource"));
            }
        }
        else{
            throw new RuntimeException("Item with id " + itemId + " not found");
        }
    }

    @Override
    public CartItemDto updateQuantity(UUID userId, Long cartId, Long id, QuantityCartItemDto quantityCartItem){
        Optional<CartItem> optionalCartItem = cartItemsDao.findById(id);
        Optional<ShoppingCart> optionalCart = shoppingCartsDao.findByUserId(userId);

        if(optionalCart.isPresent() && optionalCartItem.isPresent())
        {
            ShoppingCart shoppingCart = optionalCart.get();
            CartItem cartItem = optionalCartItem.get();

            if (shoppingCart.getId().equals(cartId) && cartItem.getCartId().getId().equals(cartId)) {
                if (quantityCartItem.getQuantity() > 0) {
                    cartItem.setQuantity(quantityCartItem.getQuantity());
                    cartItemsDao.save(cartItem);
                    return modelMapper.map(cartItem, CartItemDto.class);
                } else {
                    cartItemsDao.delete(cartItem);
                    return null;
                }
            }else{
                throw new SecurityException("You can't access this resource");
            }
        }
        else{
            throw new RuntimeException("Item with id " + id + " not found");
        }
    }
}
