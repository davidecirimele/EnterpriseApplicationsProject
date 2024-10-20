package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistItemsServiceImpl implements WishlistItemsService {

    private final WishlistItemsDao wishlistItemsDao;
    private final WishlistsDao wishlistsDao;
    private final ModelMapper modelMapper;


    @Override
    public WishlistItemDto addItemToWishlist(WishlistItem wishlistItem) {
        Wishlist wishlist = wishlistsDao.findById(wishlistItem.getWishlist().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));
        wishlistItem.setWishlist(wishlist);
        WishlistItem wi = wishlistItemsDao.save(wishlistItem);
        return modelMapper.map(wi, WishlistItemDto.class);

    }


    @Override
    public WishlistItemDto deleteItemById(Long idWishlistItem, UUID idUser) {
        WishlistItem wishlistItem = wishlistItemsDao.findById(idWishlistItem)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist item ID"));

        Wishlist wishlist = wishlistsDao.findById(wishlistItem.getWishlist().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));

        if (!wishlist.getUserId().toString() .equals  (idUser.toString()))
            throw new IllegalArgumentException("User not authorized to delete item from wishlist");


        wishlist.getItems().remove(wishlistItem);
        try {
            wishlistItemsDao.deleteById(idWishlistItem);
            return modelMapper.map(wishlistItem, WishlistItemDto.class);

            // Restituisci un codice di stato HTTP 204 (No Content) per indicare successo senza contenuto
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Invalid wishlist item ID");
        }
    }

    @Override
    public List<WishlistItemDto> getItemsByWishlistId(Long id, UUID idUser) {
        Wishlist wishlist = wishlistsDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));

        if (wishlist == null) {
            throw new IllegalArgumentException("Wishlist not found");
        }

        if (!wishlist.getUserId().getId().equals(idUser))
            throw new IllegalArgumentException("User not authorized to view items in wishlist");

        List<WishlistItem> wishlistItems = wishlistItemsDao.findByWishlistId(id);

        return wishlistItems.stream()
                .map(wi -> modelMapper.map(wi, WishlistItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<WishlistItemDto> getAllSorted() {
        if (wishlistItemsDao == null) {
            System.out.println("wishlistItemsDao is null!");
            throw new NullPointerException();
        }
        List<WishlistItem> wishlistItems = wishlistItemsDao.findAll();
        if (wishlistItems == null || wishlistItems.isEmpty()) {
            System.out.println("No wishlistItems found!");
        } else {
            System.out.println("wishlistItems found!");
        }
        return wishlistItems.stream()
                .map(wi -> modelMapper.map(wi, WishlistItemDto.class))
                .toList();
    }


    @Override
    public WishlistItemDto getById(Long id) {
        return wishlistItemsDao.findById(id)
                .map(wishlistItem -> modelMapper.map(wishlistItem, WishlistItemDto.class))
                .orElse(null);
    }

    @Override
    public void save(WishlistItem wishlistItem) {
        wishlistItemsDao.save(wishlistItem);
    }




}
