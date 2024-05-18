package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistsServiceImpl implements WishlistsService {

    private final WishlistsDao wishlistsDao;
    private final GroupsDao groupsDao;

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
    public Boolean shareWishlist(Wishlist wishlist, List<User> users) {
        List<Group> groups;
        //return wishlistsDao.shareWishlist(wishlist, users); toDO
        return false;
    }

    @Override
    public Boolean unshareWishlist(Wishlist wishlist, List<User> users) {
        //return wishlistsDao.unshareWishlist(wishlist, users);
        return false; // toDO
    }


}
