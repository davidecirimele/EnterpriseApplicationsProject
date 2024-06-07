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
    public WishlistItemDto addItemToWishlist(Long wishlistId, WishlistItem wishlistItem) {
        Wishlist wishlist = wishlistsDao.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));
        wishlistItem.setWishlist(wishlist);
        WishlistItem wi = wishlistItemsDao.save(wishlistItem);
        return modelMapper.map(wi, WishlistItemDto.class);

    }


    @Override
    public WishlistItemDto deleteByIdAndWishlistId(Long id, Long wishlistId) {
        WishlistItemDto wi =  wishlistItemsDao.deleteByIdAndWishlistId(id, wishlistId);
        return wi;
    }

    @Override
    public List<WishlistItemDto> getItemsByWishlistId(Long wishlistId) {
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
    public WishlistItemDto getById(Long id) {
        return wishlistItemsDao.findByIdDto(id);
    }




}
