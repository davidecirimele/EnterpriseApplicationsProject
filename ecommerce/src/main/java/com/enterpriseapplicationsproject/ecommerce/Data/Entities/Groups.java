package com.enterpriseapplicationsproject.ecommerce.Data.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Groups {

    @Id
    private long groupId;

    private String groupName;
}
