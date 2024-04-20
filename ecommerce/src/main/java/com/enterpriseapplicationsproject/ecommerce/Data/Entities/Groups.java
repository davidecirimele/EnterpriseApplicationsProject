package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;

@Entity
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "GROUP_NAME")
    private String groupName;
}
