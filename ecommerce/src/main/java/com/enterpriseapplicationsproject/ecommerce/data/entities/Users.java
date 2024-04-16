package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.enterpriseapplicationsproject.ecommerce.data.domain.Email;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Users")
public class Users {

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

    @Basic(optional = false)
    @Column(name = "EMAIL")
    private String email;

    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;

    @Basic(optional = false)
    @Column(name = "DEFAULT_ADDRESS")
    private String defaultAddress;

    @Basic(optional = false)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Basic(optional = false)
    @Column(name = "ROLE")
    private String role;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
