package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
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
    public WishlistItem addItemToWishlist(Long wishlistId, WishlistItem wishlistItem) {
        Wishlist wishlist = wishlistsDao.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));
        wishlistItem.setWishlist(wishlist);
        return wishlistItemsDao.save(wishlistItem);
    }


    @Override
    public Boolean deleteByIdAndWishlistId(Long id, Long wishlistId) {
        return wishlistItemsDao.deleteByIdAndWishlistId(id, wishlistId);
    }

    @Override
    public List<WishlistItem> getItemsByWishlistId(Long wishlistId) {
        return wishlistItemsDao.findByWishlistId(wishlistId);
    }

    @Override
    public List<WishlistItemDto> getAllSorted() {
        return wishlistItemsDao.findAll(Sort.by(Sort.Order.asc("wishlist")))
                .stream()
                .map(wishlistItem -> modelMapper.map(wishlistItem, WishlistItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public WishlistItem getById(Long id) {
        return wishlistItemsDao.findById(id).orElse(null);
    }




}
