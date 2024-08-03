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
    private boolean defaultAddress;

    @Column(name = "IS_VALID")
    private boolean valid;


    public boolean isDefaultAddress(){
        return defaultAddress;
    }


    public void setIsValidAddress(boolean value) {this.valid = value;}

    public void setUser(User userId) {

        this.userId = userId;
    }

    public boolean isValid() {
        return this.valid;
    }
}
