package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;


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

    @Override
    public String toString() {
        return "ADDRESS{" +
                "id=" + id +
                ", userId=" + (userId != null ? userId.getId() : null) +
                ", street=" + street +
                ", province=" + province +
                ", state=" + state +
                ", postalcode=" + postalCode +
                ", default=" + defaultAddress +
                '}';
    }
}
