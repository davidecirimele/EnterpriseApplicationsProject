package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.GroupsDao;
import com.enterpriseapplicationsproject.ecommerce.data.dao.UsersDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.GroupsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupsServiceImpl implements GroupsService {
    private final GroupsDao groupDao;
    private final UsersDao userDao;
    private final ModelMapper modelMapper;

    private final ModelMapper customMP = new ModelMapper();


    @Override
    public GroupDto createGroup(GroupDto groupDto) {
        Group group = modelMapper.map(groupDto, Group.class);
        Group savedGroup = groupDao.save(group);
        return modelMapper.map(savedGroup, GroupDto.class);
    }

    @Override
    public GroupDto findGroupById(Long id) {
        Group group = groupDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", id)));
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
    public void addUserToGroup(Long groupId, UUID userId) {
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", groupId)));
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id [%s]", userId)));

        group.getMembers().add(user);
        groupDao.save(group);
    }

    @Override
    public void removeUserFromGroup(Long groupId, UUID userId) {
        Group group = groupDao.findById(groupId)
                .orElseThrow(() -> new RuntimeException(String.format("Group not found with id [%s]", groupId)));
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id [%s]", userId)));

        group.getMembers().remove(user);
        groupDao.save(group);
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
