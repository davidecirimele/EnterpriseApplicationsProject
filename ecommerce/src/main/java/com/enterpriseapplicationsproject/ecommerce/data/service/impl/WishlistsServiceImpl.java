package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistsServiceImpl implements WishlistsService {

    private final WishlistsDao wishlistsDao;
    private final ModelMapper modelMapper;
    private final GroupsDao groupsDao;

    @Override
    public List<WishlistDto> getAllSorted() {
        return wishlistsDao.findAll(Sort.by(Sort.Order.asc("privacySetting")))
                .stream()
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Wishlist> getWishlistsByUser(User user) {
        return wishlistsDao.findByUserId(user.getId());
    }

    @Override
    public List<Wishlist> getAll() {
        return wishlistsDao.findAll();
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        return wishlistsDao.save(wishlist);
    }

    @Override
    public Group getGroupByWishlist(Wishlist wishlist) {
        return wishlistsDao.getGroupByWishlist(wishlist);
    }

    @Override
    public Boolean shareWishlist(Wishlist wishlist, Group group) {
        return wishlistsDao.shareWishlist(wishlist, group);
    }

    @Override
    public Boolean unshareWishlist(Wishlist wishlist, Group group) {
        return wishlistsDao.unshareWishlist(wishlist, group);
    }

    @Override
    public WishlistDto getById(Long id) {
        return null;
    }

    @Override
    public List<WishlistDto> getByLastname(String name) {
        return List.of();
    }
    public WishlistDto updateWishlist(Long id, WishlistDto wishlistDto) {
        return wishlistsDao.findById(id)
                .map(wishlist -> {
                    wishlist.setGroup(wishlistDto.getGroup());
                    wishlist.setItems(wishlistDto.getItems());
                    wishlist.setPrivacySetting(wishlistDto.getPrivacySetting());
                    return modelMapper.map(wishlistsDao.save(wishlist), WishlistDto.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }


}
