package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;

    private String street;

    private String province;

    private String city;

    private String state;

    private String postalCode;

    private String additionalInfo;
    
    private boolean defaultAddress;

    private boolean valid;

    public void UpdateDefaultAddress(boolean value) {
        this.defaultAddress = value;
    }

    public boolean isDefaultAddress() {
        return this.defaultAddress;
    }

    public boolean isValid(){
        return this.valid;
    }

    public void setDefaultAddress(boolean default_address) {
        this.defaultAddress = default_address;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
