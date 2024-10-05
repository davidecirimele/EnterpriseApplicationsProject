package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsDao extends JpaRepository<Group, Long> {

    Group findGroupById(Long id);

    @Query("SELECT g FROM Group g JOIN FETCH g.members WHERE g.id = :id")
    Group findGroupMembersById(Long id);

}
