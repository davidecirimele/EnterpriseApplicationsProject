package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "LASTNAME")
    private String lastName;

    @Basic(optional = false)
    @Column(name = "FIRSTNAME")
    private String firstName;

    @Basic(optional = false)
    @Column(name = "BIRTHDATE")
    private LocalDate birthDate;

    @Column(name = "PROPIC")
    private byte[] profilepPicture;

    @Embedded
    private Credential credential;

    @OneToMany(mappedBy = "userId")
    private List<Address> addresses;

    @Basic(optional = true)
    @Column(name = "DEFAULT_ADDRESS")
    private Long defaultAddress;

    @Basic(optional = false)
    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @Basic(optional = false)
    @Column(name = "ROLE")
    private String role;

    /*@PrePersist
    public void prePersist() {
        if (addresses != null && !addresses.isEmpty()) {
            defaultAddress = addresses.get(0).getId();
        }
    }*/

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
