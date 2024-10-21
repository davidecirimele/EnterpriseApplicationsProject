package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistItemsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.WishlistsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistsServiceImpl implements WishlistsService {

    private final WishlistsDao wishlistsDao;
    private final WishlistItemsDao WIDao;
    private final ModelMapper modelMapper; // serve per fare il mapping tra due oggetti
    private final GroupsDao groupsDao;
    private final UsersDao usersDao;

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
    public List<WishlistDto> getFriendWishlists(UUID userId) {
        return wishlistsDao.findFriendWishlists(userId)
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
        // Verifica se la wishlistDto è null
        if (wishlistDto == null) {
            throw new IllegalArgumentException("WishlistDto is null");
        }

        // Controlla il campo 'name'
        if (wishlistDto.getName() == null || wishlistDto.getName().isBlank()) {
            throw new IllegalArgumentException("Wishlist name is null or empty");
        }

        // Controlla il campo 'privacySetting'
        if (wishlistDto.getPrivacySetting() == null) {
            throw new IllegalArgumentException("Wishlist privacy setting is null");
        }

        // Controlla il campo 'user'
        if (wishlistDto.getUser() == null) {
            throw new IllegalArgumentException("Wishlist user is null");
        }

        // Gestione del gruppo associato alla wishlist
        GroupDto groupDto = wishlistDto.getGroup();
        Group group;

        if (groupDto == null) {
            // Se non viene passato un gruppo, creane uno di default
            group = new Group();
            group.setGroupName("Group " + wishlistDto.getName());
            group = groupsDao.save(group);  // Salva il gruppo nel database
            groupDto = modelMapper.map(group, GroupDto.class);
        } else {
            // Se viene passato un gruppo, lo mappiamo
            group = modelMapper.map(groupDto, Group.class);

            // Verifica se il gruppo ha membri, se mancano aggiungi l'utente come unico membro
            if (groupDto.getMembers() == null) {
                group.setMembers(Collections.emptyList());
            }
            else {
                // Mappatura dei membri del gruppo
                List<User> members = groupDto.getMembers().stream()
                        .map(userDto -> modelMapper.map(userDto, User.class))
                        .toList();
                group.setMembers(members);
            }
            // Salva o aggiorna il gruppo con i membri
            group = groupsDao.save(group);
        }

        // Imposta o genera il token per la wishlist
        String wToken = wishlistDto.getWishlistToken();
        if (wToken == null || wToken.isBlank()) {
            wToken = generateWToken();  // Genera un token se non è presente
        }

        // Mappatura e creazione della wishlist
        Wishlist wishlist = modelMapper.map(wishlistDto, Wishlist.class);
        wishlist.setName(wishlistDto.getName());
        wishlist.setPrivacySetting(wishlistDto.getPrivacySetting());
        wishlist.setGroup(group);  // Assegna il gruppo alla wishlist
        wishlist.setWishlistToken(wToken);

        // Salva la wishlist nel database
        Wishlist savedWishlist = wishlistsDao.save(wishlist);

        // Log e ritorno della wishlist salvata
        System.out.println("Dati nuova Wishlist: " + savedWishlist.toString());
        return modelMapper.map(savedWishlist, WishlistDto.class);
    }


    @Override
    public Group getGroupByWishlistId(Long wishlistId) {
        return wishlistsDao.getGroupByWishlistId(wishlistId);
    }

    @Override
    @Transactional
    public Boolean JoinShareWishlist(UUID idUserToJoin, String wToken) {
        User user = usersDao.findById(idUserToJoin)
                .orElseThrow( () -> new IllegalArgumentException("Invalid Owner email user"));

        System.out.println("Token: " + wToken);

        Wishlist wishlistToJoin = wishlistsDao.findWishlistByWishlistToken(wToken);

        if(wishlistToJoin.getUserId().equals(idUserToJoin)){
            throw new IllegalArgumentException("User is the owner of the wishlist");
        }

        Group group = wishlistToJoin.getGroup();

        if (group == null) {
            group = new Group();
            group.setGroupName("Group " + wishlistToJoin.getName());
        }
        if (group.getMembers().contains(user)){
            throw new IllegalArgumentException("User is already in the group");
        }

        group.getMembers().add(user);
        groupsDao.save(group);

        wishlistToJoin.setGroup(group);
        wishlistsDao.save(wishlistToJoin);
        return true;
    }

    @Override
    @Transactional
    public Boolean unshareWishlist(Long wishlistId, UUID idUser) {
        Wishlist wishlist = wishlistsDao.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wishlist ID"));

        if (wishlist == null){
            throw new IllegalArgumentException("Wishlist not found");
        }
        if (wishlist.getUserId().toString() .equals(idUser.toString())){
            throw new IllegalArgumentException("User is the owner of the wishlist");
        }
        Group group = wishlist.getGroup();

        if (group == null || group.getMembers().stream().noneMatch(user -> user.getId().equals(idUser))) {
            return false;
        }
        boolean removed = group.getMembers().removeIf(user -> user.getId().equals(idUser));
        if (!removed){
            return false;
        }
        groupsDao.save(group);
        wishlist.setGroup(group);
        wishlistsDao.save(wishlist);
        return true;
    }


    @Override
    public WishlistDto getDtoById(Long id) {
        return wishlistsDao.findById(id).stream()
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .toList().get(0);
    /*
        return wishlistsDao.findById(id)
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));*/
    }

    /*
    @Override
    public Wishlist getById(Long id) {
        Wishlist wishlist = modelMapper.map(wishlistDto, Wishlist.class);
        return wishlistsDao.findById(id);
    }*/

    @Override
    public List<WishlistDto> getByLastname(String name) {
        return List.of();
    }

    @Override
    public void deleteWishlist(Long id) {
        wishlistsDao.deleteById(id);
    }

    @Override
    public WishlistDto deleteWishlistByID(Long id, UUID idUser) {
        // Trova la Wishlist usando il suo ID
        Wishlist w = wishlistsDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

        if (w == null) {
            throw new IllegalArgumentException("Wishlist not found");
        }

        if (!w.getUserId().toString() .equals (idUser.toString())) {
            throw new IllegalArgumentException("User not authorized to delete this wishlist");
        }

        // Elimina tutti gli elementi associati alla Wishlist
        List<WishlistItem> items = WIDao.findByWishlistId(id);
        WIDao.deleteAll(items);

        // Elimina la Wishlist
        try {
            wishlistsDao.delete(w);
            return modelMapper.map(w, WishlistDto.class);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Invalid wishlist ID");
        }
    }

    @Override
    public WishlistDto getWishlistByToken(String token) {
        Wishlist w = wishlistsDao.findWishlistByWishlistToken(token);
        return modelMapper.map(w,WishlistDto.class);
    }


    @Transactional
    @Override
    public WishlistDto updateWishlist(WishlistDto wishlistDto) {
        return wishlistsDao.findById(wishlistDto.getId())
                .map(wishlist -> {
                    wishlist.setName(wishlistDto.getName());

                    System.out.println("PS in arrivo : " + wishlistDto.getPrivacySetting());
                    System.out.println("PS attuale : " + wishlist.getPrivacySetting());

                    wishlist.setPrivacySetting(wishlistDto.getPrivacySetting());

                    Wishlist savedWishlist = wishlistsDao.save(wishlist);
                    System.out.println("PS Nuovo : " + savedWishlist.getPrivacySetting());
                    return modelMapper.map(savedWishlist, WishlistDto.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }

    public String generateWToken() {
        UUID uuid = UUID.randomUUID();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuid.toString().getBytes());
    }



}