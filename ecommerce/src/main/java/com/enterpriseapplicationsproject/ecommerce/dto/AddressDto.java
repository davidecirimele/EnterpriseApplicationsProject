package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.regex.qual.Regex;
import org.springframework.format.annotation.NumberFormat;

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

    public boolean isValid(){
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
