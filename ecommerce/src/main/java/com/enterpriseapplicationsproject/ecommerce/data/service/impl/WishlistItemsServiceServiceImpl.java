package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistItemsServiceServiceImpl implements WishlistItemsService {

    private final WishlistItemsDao wishlistItemsDao;


    @Override
    public WishlistItem addItemToWishlist(WishlistItem wishlistItem) {
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


}
