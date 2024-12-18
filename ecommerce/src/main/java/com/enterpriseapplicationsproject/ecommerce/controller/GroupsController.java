package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.config.security.RateLimit;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.GroupsService;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GroupsController {

    private final GroupsService groupsService;

    @RateLimit
    @GetMapping("/getById/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN') ")
    public ResponseEntity<List<GroupDto>> getAllGroupsByUser(@PathVariable("idUser") UUID idUser) {
        List<GroupDto> groups = groupsService.findGroupsByUser(idUser);
        if (groups != null) {
            return ResponseEntity.ok(groups);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @RateLimit
    @GetMapping("/getMembers/{idGroup}/{idUser}")
    @PreAuthorize(" #idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getMembersById(@PathVariable("idGroup") Long idGroup, @PathVariable("idUser") UUID idUser) {
        List<UserDto> members = groupsService.findMembersByGroup(idGroup);
        if (members != null) {
            return ResponseEntity.ok(members);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }



    @RateLimit
    @PostMapping("/addUser/{idUser}/{token}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Integer> addUser(@PathVariable("idUser") UUID idUser, @PathVariable("token") String token) {
        int resp = groupsService.addUserToGroup(idUser, token);
        if (resp == 1)  //aggiunto e non privata
            return ResponseEntity.ok(1);
        if (resp == 0)  //aggiunto e privata
            return ResponseEntity.ok(0);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(-1); //non aggiunto
    }


    @RateLimit
    @DeleteMapping("/removeUser/{idGroup}/{idUser}/{idUsrLogged}")
    @PreAuthorize("#idUsrLogged == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> removeUser(@PathVariable("idGroup") Long idGroup, @PathVariable("idUser") UUID idUser, @PathVariable("idUsrLogged") UUID idUsrLogged) {
        boolean resp = groupsService.removeUserFromGroup(idGroup, idUser, idUsrLogged);
        if (resp)
            return ResponseEntity.ok(true);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')") //never used yet
    public ResponseEntity<GroupDto> update(@PathVariable("groupId") Long id, @RequestBody GroupDto groupDto) {
        GroupDto updatedGroup = groupsService.updateGroup(id, groupDto);
        return ResponseEntity.ok(updatedGroup);
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')") //never used yet
    public ResponseEntity<GroupDto> add(@RequestBody GroupDto groupDto) {
        GroupDto newGroup = groupsService.createGroup(groupDto);
        return ResponseEntity.ok(newGroup);
    }


    @RateLimit
    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')") //never used yet
    public ResponseEntity<Void> delete(@PathVariable("groupId") Long id) {
        groupsService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }




}
