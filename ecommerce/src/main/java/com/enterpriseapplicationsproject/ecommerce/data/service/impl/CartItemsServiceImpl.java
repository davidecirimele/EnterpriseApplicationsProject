package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.*;
import com.enterpriseapplicationsproject.ecommerce.data.dao.CartItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.ShoppingCartsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.CartItemsService;
import com.enterpriseapplicationsproject.ecommerce.data.service.ShoppingCartService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.ResourceNotFoundException;
import com.enterpriseapplicationsproject.ecommerce.exception.ShoppingCartNotFoundException;
import com.enterpriseapplicationsproject.ecommerce.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ShoppingCartService shoppingCartService;

    @Override
    public List<CartItemDto> getCartItems(UUID userId, Long cartId) {
        try {

            ShoppingCart cart = shoppingCartsDao.findByUserId(userId).orElseThrow(()->new ShoppingCartNotFoundException("Shopping Cart not found"));

            if (cart.getId().equals(cartId)) {
                List<CartItem> cartItems = cartItemsDao.findByCartId(cart);
                return cartItems.stream().map(cartItem -> modelMapper.map(cartItem, CartItemDto.class)).toList();
            } else {
                throw new UnauthorizedAccessException("You can't access this resource");
            }
        }catch(Exception e){
            log.error("Unexpected error while fetching cart items for user with ID: "+userId+", " + e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public CartItemDto save(CartItem cartitem) {
        try {
            CartItem savedCartItem = cartItemsDao.save(cartitem);
            return modelMapper.map(savedCartItem, CartItemDto.class);
        }catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while saving cart item: {}", cartitem, e);
            throw new IllegalArgumentException("Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while saving cart item: {}", cartitem, e);
            throw new RuntimeException("Unexpected error occurred while saving cart item");
        }
    }

    @Override
    @Transactional
    public CartItemDto insert(QuantityCartItemDto insertCartItemDto, UUID userId, Long cartId, Long bookId) {
        try {
            ShoppingCart cart = shoppingCartsDao.findByUserId(userId).orElseThrow(()->new ShoppingCartNotFoundException("Shopping cart not found"));
            Book book = booksDao.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Resource not found"));

            if (cart.getId().equals(cartId)) {
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
                cart.getCartItems().add(cartItem);
                CartItem insertedItem = cartItemsDao.save(cartItem);
                return modelMapper.map(insertedItem, CartItemDto.class);
            } else {
                throw new UnauthorizedAccessException("You can't access this resource");
            }
        }catch(Exception e){
            log.error("Unexpected error while inserting cart item: " + e);
            throw new RuntimeException("Unexpected error occurred while inserting cart item");
        }
    }

    @Override
    public boolean delete(UUID userId, Long cartId, Long itemId) {
        try {
            ShoppingCart shoppingCart = shoppingCartsDao.findByUserId(userId).orElseThrow(()->new ShoppingCartNotFoundException("Shopping cart not found"));
            CartItem cartItem = cartItemsDao.findById(itemId).orElseThrow(()->new ResourceNotFoundException("Resource not found"));


            log.info("found shoppingCart: " + shoppingCart.getId() + ", passed cartID: " + cartId + ", passed itemId: " + itemId + "foundItemCartID: " + cartItem.getCartId());

            if (shoppingCart.getId().equals(cartId) && cartItem.getCartId().getId().equals(cartId)) {
                //cartItemsDao.delete(optionalCartItem.get());

                shoppingCart.getCartItems().remove(cartItem);
                shoppingCartsDao.save(shoppingCart);
                return true;
            } else {
                throw new UnauthorizedAccessException("You can't access this resource");
            }
        }catch(Exception e){
            log.error("Unexpected error while deleting item from shopping cart with ID: {}", cartId, e);
            throw new RuntimeException("Unexpected error occurred while deleting item from shopping cart");
        }
    }

    @Override
    public CartItemDto updateQuantity(UUID userId, Long cartId, Long id, QuantityCartItemDto quantityCartItem){
        try {
            CartItem cartItem = cartItemsDao.findById(id).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
            ShoppingCart shoppingCart = shoppingCartsDao.findByUserId(userId).orElseThrow(()->new ShoppingCartNotFoundException("Shopping cart not found"));


            if (shoppingCart.getId().equals(cartId) && cartItem.getCartId().getId().equals(cartId)) {
                if (quantityCartItem.getQuantity() > 0) {
                    cartItem.setQuantity(quantityCartItem.getQuantity());
                    cartItemsDao.save(cartItem);
                    //shoppingCartsDao.save(shoppingCart);

                    return modelMapper.map(cartItem, CartItemDto.class);
                } else {
                    cartItemsDao.delete(cartItem);
                    return null;
                }
            } else {
                throw new UnauthorizedAccessException("You can't access this resource");
            }
        }catch(Exception e){
            log.error("Unexpected error while updating quantity for item with ID: {}", id, e);
            throw new RuntimeException("Unexpected error occurred while updating quantity for a cart item");
        }
    }
}
