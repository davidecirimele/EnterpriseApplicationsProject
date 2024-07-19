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
    
    private boolean default_address;

    private boolean is_valid;

    public void UpdateDefaultAddress(boolean value) {
        this.default_address = value;
    }

    public boolean getDefaultAddress() {
        return this.default_address;
    }

    public boolean isValid(){
        return this.is_valid;
    }

    public void setDefaultAddress(boolean default_address) {
        this.default_address = default_address;
    }

    public void setValid(boolean valid) {
        this.is_valid = valid;
    }
}
