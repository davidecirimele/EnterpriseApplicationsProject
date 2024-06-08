package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistItemDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public WishlistItemDto deleteItemById(WishlistItem wishlistItem) {
        Wishlist wishlist = wishlistsDao.findById(wishlistItem.getWishlist().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));

        if (!wishlist.getItems().contains(wishlistItem))
            throw new IllegalArgumentException("Item not in wishlist");

        wishlistItem.setWishlist(null);
        wishlist.getItems().remove(wishlistItem);
        WishlistItem wi = wishlistItemsDao.save(wishlistItem);
        return modelMapper.map(wi, WishlistItemDto.class);
    }

    @Override
    public List<WishlistItemDto> getItemsByWishlistId(Long id) {
        return wishlistItemsDao.findByWishlistId(id);
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
                .map(s -> modelMapper.map(s, WishlistItemDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public WishlistItemDto getById(Long id) {
        return wishlistItemsDao.findById(id)
                .map(wishlistItem -> modelMapper.map(wishlistItem, WishlistItemDto.class))
                .orElse(null);
    }




}
