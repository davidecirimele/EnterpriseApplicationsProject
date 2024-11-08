package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface GroupsService {
    GroupDto createGroup(GroupDto groupDto);

    GroupDto findGroupById(Long id, UUID idUser);

    List<UserDto> findMembersByGroup(Long idGroup);

    List<GroupDto> findGroupsByUser(UUID idUser);

    GroupDto updateGroup(Long id, GroupDto groupDto);

    void deleteGroup(Long id);

    List<GroupDto> getAllGroups();

    int addUserToGroup(UUID idUser, String token);

    boolean removeUserFromGroup(Long groupId, UUID userId, UUID idUsrLogged);

    Group getGroupById(Long id);

    void save(Group group);
}
