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

    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotBlank(message = "Street name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Street name must contain only letters and numbers")
    private String street;

    @NotBlank(message = "Province name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Province name must contain only letters and spaces")
    private String province;

    @NotBlank(message = "City name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Province name must contain only letters and spaces")
    private String city;

    @NotBlank(message = "State name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "State name must contain only letters and spaces")
    private String state;

    @NotBlank
    @Pattern(regexp = "\\d{4,6}", message = "Postal code must be between 4 and 6 digits")
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
