package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ROLE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Basic(optional = false)
    @Column(name = "LASTNAME")
    private String lastName;

    @Basic(optional = false)
    @Column(name = "FIRSTNAME")
    private String firstName;

    @Basic(optional = false)
    @Column(name = "BIRTHDATE")
    private LocalDate birthDate;

    @Embedded
    private Credential credential;

    @OneToMany(mappedBy = "userId") // mappedBy indica il nome dell'attributo nella classe Address
    private List<Address> addresses;

    @Basic(optional = false)
    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER) // mappedBy indica il nome dell'attributo nella classe Group
    private List<Group> groups = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Credential getCredential() {
        if(this.credential == null)
            this.credential = new Credential();
        return this.credential;
    }

    public void setCredentials(String email, String password) {
        this.credential = new Credential();
        this.credential.setEmail(email);
        this.credential.setPassword(password);
    }

}
