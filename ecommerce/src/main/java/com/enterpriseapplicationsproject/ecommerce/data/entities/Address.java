package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Addresses")
@Data
@NoArgsConstructor
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID") // name indica il nome della colonna nella tabella Address
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

    public boolean is_default_address(){
        return default_address;
    }

    public void setDefaultAddress(boolean value) {
        this.default_address = value;
    }

    public void setIsValidAddress(boolean value) {this.is_valid = value;}

    public void setUser(User userId) {

        System.out.println("USEEEER : "+userId);
        this.userId = userId;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.postalCode = zipCode;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public boolean isValid() {
        return this.is_valid;
    }
}
