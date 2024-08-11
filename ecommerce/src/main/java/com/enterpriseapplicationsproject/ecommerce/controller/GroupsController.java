package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.GroupsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GroupsController {

    private final GroupsService groupsService;

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> getById(@PathVariable("groupId") Long id) {
        GroupDto group = groupsService.findGroupById(id);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDto> update(@PathVariable("groupId") Long id, @RequestBody GroupDto groupDto) {
        GroupDto updatedGroup = groupsService.updateGroup(id, groupDto);
        return ResponseEntity.ok(updatedGroup);
    }

    @PostMapping()
    public ResponseEntity<GroupDto> add(@RequestBody GroupDto groupDto) {
        GroupDto newGroup = groupsService.createGroup(groupDto);
        return ResponseEntity.ok(newGroup);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> delete(@PathVariable("groupId") Long id) {
        groupsService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable("groupId") Long groupId, @PathVariable("userId") UUID userId) {
        groupsService.addUserToGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable("groupId") Long groupId, @PathVariable("userId") UUID userId) {
        groupsService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }
}
