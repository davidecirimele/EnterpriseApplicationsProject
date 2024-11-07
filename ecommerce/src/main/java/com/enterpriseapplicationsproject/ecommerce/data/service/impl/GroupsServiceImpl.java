package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.entities.WishlistPrivacy;
import com.enterpriseapplicationsproject.ecommerce.data.service.GroupsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupsServiceImpl implements GroupsService {

    private final GroupsDao groupDao;
    private final UsersDao userDao;
    private final WishlistsDao wishlistDao;
    private final ModelMapper modelMapper;

    private final ModelMapper customMP = new ModelMapper();


    @Override
    public GroupDto createGroup(GroupDto groupDto) {
        Group group = modelMapper.map(groupDto, Group.class);
        Group savedGroup = groupDao.save(group);
        return modelMapper.map(savedGroup, GroupDto.class);
    }

    @Override
    public GroupDto findGroupById(Long id, UUID idUser) {
        Group group = groupDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", id)));
        Wishlist wishlist = wishlistDao.findWishlistByGroup_Id(id);

        if (!group.getMembers().stream().anyMatch(user -> user.getId().equals(idUser))
                        && !wishlist.getUserId().equals(idUser)) {
            throw new IllegalArgumentException("User is not a member or the owner of the group");
        }
        return modelMapper.map(group, GroupDto.class);
    }

    @Override
    public List<UserDto> findMembersByGroup(Long idGroup) {
        return groupDao.findById(idGroup)
                .map(group -> group.getMembers().stream()
                        .map(user -> modelMapper.map(user, UserDto.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", idGroup)));
    }

    @Override
    public List<GroupDto> findGroupsByUser(UUID idUser) {
        List<Wishlist> wishlists = wishlistDao.findByUserId(idUser);

        List<Group> groups = wishlists.stream()
                .map(Wishlist::getGroup)
                .toList();

        return groups.stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto updateGroup(Long id, GroupDto groupDto) {
        Group group = groupDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", id)));
        modelMapper.map(groupDto, group);
        Group updatedGroup = groupDao.save(group);
        return modelMapper.map(updatedGroup, GroupDto.class);
    }

    @Override
    public void deleteGroup(Long id) {
        groupDao.deleteById(id);
    }


    @Override
    public List<GroupDto> getAllGroups() {
        ModelMapper customMP = new ModelMapper();
        customMP.typeMap(PersistentBag.class, List.class)
                .setConverter(new Converter<PersistentBag, List>() {
                    @Override
                    public List convert(MappingContext<PersistentBag, List> context) {
                        return List.of();
                    }
                });

        List<Group> groups = groupDao.findAll();
        return groups.stream()
                .map(group -> customMP.map(group, GroupDto.class))
                .collect(Collectors.toList());
    }
    /*
    @Override
    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupDao.findAll();
        return groups.stream()
                .map(group -> {
                    GroupDto dto = new GroupDto();
                    dto.setId(group.getId());
                    dto.setGroupName(group.getGroupName());

                    // Mappatura manuale dei membri
                    List<UserDto> membersDto = group.getMembers().stream()
                            .map(user -> {
                                UserDto userDto = new UserDto();
                                userDto.setId(user.getId());
                                userDto.setFirstName(user.getFirstName());
                                userDto.setLastName(user.getLastName());
                                userDto.setBirthDate(user.getBirthDate());

                                // Aggiungi altre proprietà come necessario
                                return userDto;
                            })
                            .collect(Collectors.toList());

                    dto.setMembers(membersDto);
                    return dto;
                })
                .collect(Collectors.toList());
    }*/

    @Override
    @Transactional
    public int addUserToGroup(UUID idUser, String wToken) {
        Wishlist wishlistToJoin = wishlistDao.findWishlistByWishlistToken(wToken);

        if (wishlistToJoin == null) {
            throw new IllegalArgumentException("Wishlist not found");
        }

        if(wishlistToJoin.getUserId() .equals (idUser)){
            throw new IllegalArgumentException("User is the owner of the wishlist");
        }
        Long groupId = wishlistToJoin.getGroup().getId();

        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", groupId)));

        if (group == null) {
            group = new Group();
            group.setGroupName("Group " + wishlistToJoin.getName());
        }
        else if(isAMember(group, idUser)){
            throw new IllegalArgumentException("User is already in the group");
        }

        User user = userDao.findById(idUser)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id [%s]", idUser)));


        group.getMembers().add(user);
        groupDao.save(group);
        System.out.println("Group saved");

        wishlistToJoin.setGroup(group);
        System.out.println("Group set");

        wishlistDao.save(wishlistToJoin);
        System.out.println("Wishlist saved");
        if (wishlistToJoin.getPrivacySetting().equals(WishlistPrivacy.PRIVATE)){
            return 0;
        }
        return 1;
    }

    @Override
    @Transactional
    public boolean removeUserFromGroup(Long groupId, UUID idUser, UUID idUsrLogged) {
        Wishlist wishlist = wishlistDao.findWishlistByGroup_Id(groupId);
        if (wishlist == null) {
            throw new IllegalArgumentException("Wishlist not found");
        }
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", groupId)));


        if (!idUsrLogged.equals(wishlist.getUserId().getId())) {
            if (!isAMember(group, idUsrLogged)) {
                throw new IllegalArgumentException("User is not a member or the owner of the group");
            }
        }


        group.getMembers().remove(userDao.findById(idUser)
                .orElseThrow(() -> new RuntimeException(String.format("User not in the group or not found", idUser))));
        groupDao.save(group);

        if(!idUsrLogged.equals(idUser)){ //Se l'utente viene cacciato dal gruppo, cambia il token della wishlist
            wishlist.setWishlistToken(generateWToken());
        }
        wishlistDao.save(wishlist);

        System.out.println("Token changed");
        return true;
    }

    private boolean isAMember(Group group, UUID idUser) {
        if (group == null || idUser == null) {
            return false;
        }
        return group.getMembers().stream().anyMatch(user -> user.getId().equals(idUser));


    }

    private String generateWToken() {
        UUID uuid = UUID.randomUUID();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuid.toString().getBytes());
    }

    @Override
    public Group getGroupById(Long id) {
        return groupDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", id)));
    }

    @Override
    public void save(Group group) {
        groupDao.save(group);
    }



}
