package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Addresses")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User userId;

    @Column(name = "STREET")
    private String street;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "ADDITIONAL_INFO")
    private String additionalInfo;

    @Column(name = "DEFAULT_ADDRESS")
    private boolean default_address;

    @Column(name = "IS_VALID")
    private boolean is_valid;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDefaultAddress(boolean value) {
        this.default_address = value;
    }

    public void setIsValidAddress(boolean value) {this.is_valid = value;}
}
