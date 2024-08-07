package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;

import java.util.List;
import java.util.UUID;

public interface GroupsService {
    GroupDto createGroup(GroupDto groupDto);

    GroupDto findGroupById(Long id);

    GroupDto updateGroup(Long id, GroupDto groupDto);

    void deleteGroup(Long id);

    List<GroupDto> getAllGroups();

    void addUserToGroup(Long groupId, UUID userId);

    void removeUserFromGroup(Long groupId, UUID userId);
}
