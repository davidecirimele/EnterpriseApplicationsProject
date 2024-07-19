package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.enterpriseapplicationsproject.ecommerce.dto.AddressDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ROLE", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "FIRSTNAME")
    private String firstName;

    @Basic(optional = false)
    @Column(name = "LASTNAME")
    private String lastName;

    @Basic(optional = false)
    @Column(name = "BIRTHDATE")
    private LocalDate birthDate;

    @Column(name = "PROPIC")
    private byte[] profilePicture;

    @Embedded
    private Credential credential;

    @OneToMany(mappedBy = "userId")
    private List<Address> addresses;

    @Basic(optional = false)
    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @ManyToMany(mappedBy = "members")
    private List<Group> groups = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {return this.lastName;}

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        System.out.println("User Last Name: " + this.getLastName());
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = date;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Credential getCredential() {
        if(this.credential == null)
            this.credential = new Credential();
        return this.credential;
    }

    public void setEmail(String email) {
        if (this.credential == null) {
            this.credential = new Credential();
        }
        this.credential.setEmail(email);
    }

    public void setCredentials(String email, String password) {
        this.credential = new Credential();
        this.credential.setEmail(email);
        this.credential.setPassword(password);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public byte[] getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    // Getter e Setter per addresses
    public List<Address> getAddresses() {
        return addresses;
    }
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

}
