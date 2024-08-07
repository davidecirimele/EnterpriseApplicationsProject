package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import com.enterpriseapplicationsproject.ecommerce.dto.WishlistDto;

import java.util.List;

public interface GroupsService {
    GroupDto createGroup(GroupDto groupDto);

    GroupDto findGroupById(Long id);

    GroupDto updateGroup(Long id, GroupDto groupDto);

    void deleteGroup(Long id);

    List<GroupDto> getAllGroups();

//    void addUserToGroup(Long groupId, Long userId);
//
//    void removeUserFromGroup(Long groupId, Long userId);
}
