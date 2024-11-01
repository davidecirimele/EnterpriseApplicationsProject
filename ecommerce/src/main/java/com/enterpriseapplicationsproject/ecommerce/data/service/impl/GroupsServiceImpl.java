package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.WishlistsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.data.service.GroupsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

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
    public boolean addUserToGroup(UUID idUser, String wToken) {
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

        User user = userDao.findById(idUser)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id [%s]", idUser)));

        List<User> members = group.getMembers();

        if (members.contains(user)){
            throw new IllegalArgumentException("User is already in the group");
        }

        members.add(user);
        groupDao.save(group);
        System.out.println("Group saved");

        wishlistToJoin.setGroup(group);
        System.out.println("Group set");
        wishlistDao.save(wishlistToJoin);
        System.out.println("Wishlist saved");
        return true;
    }

    @Override
    @Transactional
    public boolean removeUserFromGroup(Long groupId, UUID idUser) {

        Wishlist wishlist = wishlistDao.findWishlistByGroup_Id(groupId);

        User user = userDao.findById(idUser)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id [%s]", idUser)));

        if (wishlist == null) {
            throw new IllegalArgumentException("Wishlist not found");
        }

        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", groupId)));

        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        List<User> members = group.getMembers();

        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Group has no members");
        }

        if (!members.stream().anyMatch(usr -> usr.getId().equals(idUser))) {
            throw new IllegalArgumentException("User is not in the group");
        }

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Verifica se l'utente ha il ruolo ADMIN, da vedere se ha senso
//        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        if (wishlist.getUserId().equals(idUser) ) {
            throw new IllegalArgumentException("User is the owner of the wishlist");

        }
        members.remove(userDao.findById(idUser)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id [%s]", idUser))));
        groupDao.save(group);

        return true;
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
