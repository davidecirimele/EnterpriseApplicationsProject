package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final ModelMapper modelMapper; // serve per fare il mapping tra due oggetti
    private final GroupsDao groupsDao;

    @Override
    public List<WishlistDto> getAllSorted() { // restituisce la lista di tutti i wishlists ordinata per privacySetting
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
    public void save(Wishlist wishlist) {
       wishlistsDao.save(wishlist);
    }

    @Override
    public WishlistDto save(WishlistDto wishlistDto) {
        Wishlist wishlist = modelMapper.map(wishlistDto, Wishlist.class);
        Wishlist w = wishlistsDao.save(wishlist);
        return modelMapper.map(w, WishlistDto.class);
    }


    @Override
    public Group getGroupByWishlistId(Long wishlistId) {
        return wishlistsDao.getGroupByWishlistId(wishlistId);
    }

    @Override
    @Transactional
    public Boolean shareWishlist(Long wishlistId, Group group) {
        Wishlist wishlist = wishlistsDao.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));

        wishlist.setGroup(group);
        wishlistsDao.save(wishlist);
        return true;
    }

    @Override
    @Transactional
    public Boolean unshareWishlist(Long wishlistId) {
        Wishlist wishlist = wishlistsDao.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));

        wishlist.setGroup(null);
        wishlistsDao.save(wishlist);
        return true;
    }

    @Override
    public WishlistDto getById(Long id) {
        return wishlistsDao.findById(id)
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }

    @Override
    public List<WishlistDto> getByLastname(String name) {
        return List.of();
    }

    @Override
    public void deleteWishlist(Long id) {
        wishlistsDao.deleteById(id);
    }

    @Transactional
    @Override
    public WishlistDto updateWishlist(Long id, WishlistDto wishlistDto) {
        return wishlistsDao.findById(id)
                .map(wishlist -> {
                    wishlist.setGroup(wishlistDto.getGroup());
                    wishlist.setItems(wishlistDto.getItems()); //tecnicamente lo fa gia' WishlitstItem server, dovrei lasciarlo?
                    wishlist.setPrivacySetting(wishlistDto.getPrivacySetting());
                    return modelMapper.map(wishlistsDao.save(wishlist), WishlistDto.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }


}
