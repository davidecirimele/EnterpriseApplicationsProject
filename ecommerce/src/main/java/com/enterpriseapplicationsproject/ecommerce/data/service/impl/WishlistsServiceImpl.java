package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistItem;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistsServiceImpl implements WishlistsService {

    private final WishlistsDao wishlistsDao;
    private final WishlistItemsDao WIDao;
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
    public List<WishlistDto> getWishlistsByUser(UUID userId) {
        return wishlistsDao.findByUserId(userId)
                .stream()
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .collect(Collectors.toList());
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
        return wishlistsDao.findById(id).stream()
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .toList().get(0);
    /*
        return wishlistsDao.findById(id)
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));*/
    }

    @Override
    public List<WishlistDto> getByLastname(String name) {
        return List.of();
    }

    @Override
    public void deleteWishlist(Long id) {
        wishlistsDao.deleteById(id);
    }

    @Override
    public WishlistDto deleteWishlistByID(Long id) {
        // Trova la Wishlist usando il suo ID
        Wishlist w = wishlistsDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

        // Elimina tutti gli elementi associati alla Wishlist
        List<WishlistItem> items = WIDao.findByWishlistId(id);
        for (WishlistItem item : items) {
            WIDao.delete(item);
        }

        // Elimina la Wishlist
        try {
            wishlistsDao.delete(w);
            return modelMapper.map(w, WishlistDto.class);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Invalid wishlist ID");
        }
    }


    @Transactional
    @Override
    public WishlistDto updateWishlist(Long id, WishlistDto wishlistDto) {
        return wishlistsDao.findById(id)
                .map(wishlist -> {
                    wishlist.setName(wishlistDto.getName());
                    wishlist.setPrivacySetting(wishlistDto.getPrivacySetting());

                    // Mappa gli items
                    List<WishlistItem> updatedItems = wishlistDto.getItems().stream()
                            .map(itemDto -> {
                                WishlistItem item = modelMapper.map(itemDto, WishlistItem.class);
                                item.setWishlist(wishlist);
                                return item;
                            })
                            .collect(Collectors.toList());

                    // Sostituisci gli items esistenti con quelli aggiornati
                    wishlist.getItems().clear();
                    wishlist.getItems().addAll(updatedItems);

                    // Mappa il group
                    GroupDto groupDto = wishlistDto.getGroup();
                    if (groupDto != null && groupDto.getId() != null) {
                        Group group = groupsDao.findById(groupDto.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
                        wishlist.setGroup(group);
                    } else {
                        wishlist.setGroup(null);
                    }

                    Wishlist savedWishlist = wishlistsDao.save(wishlist);
                    return modelMapper.map(savedWishlist, WishlistDto.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }



}
