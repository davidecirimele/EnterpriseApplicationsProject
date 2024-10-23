package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.service.GroupsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GroupsController {

    private final GroupsService groupsService;

    @RateLimit
    @GetMapping("/getById/{idUser}/{idGroup}")
    @PreAuthorize(" #idUser == authentication.principal.getId() or hasRole('ADMIN') ")
    public ResponseEntity<GroupDto> getById(@PathVariable("idUser") UUID idUser, @PathVariable("idGroup") Long id) {
        GroupDto group = groupsService.findGroupById(id, idUser);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> update(@PathVariable("groupId") Long id, @RequestBody GroupDto groupDto) {
        GroupDto updatedGroup = groupsService.updateGroup(id, groupDto);
        return ResponseEntity.ok(updatedGroup);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> add(@RequestBody GroupDto groupDto) {
        GroupDto newGroup = groupsService.createGroup(groupDto);
        return ResponseEntity.ok(newGroup);
    }

    @RateLimit
    @PostMapping("/addUser/{idUser}/{token}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> addUser(@PathVariable("idUser") UUID idUser, @PathVariable("token") String token) {
        boolean resp = groupsService.addUserToGroup(idUser, token);
        if (resp)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RateLimit
    @DeleteMapping("/removeUser/{idGroup}/{idUser}")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> removeUser(@PathVariable("idGroup") Long idGroup, @PathVariable("idUser") UUID idUser) {
        boolean resp = groupsService.removeUserFromGroup(idGroup, idUser);
        if (resp)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RateLimit
    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("groupId") Long id) {
        groupsService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }




}
