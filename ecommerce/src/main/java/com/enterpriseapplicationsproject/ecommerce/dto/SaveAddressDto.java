package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.util.UUID;

@Data
public class SaveAddressDto {

    @NotBlank(message = "street is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Street name must contain only letters and numbers")
    private String street;

    @NotBlank(message = "province is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Province name must contain only letters and spaces")
    private String province;

    @NotBlank(message = "city is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "City name must contain only letters and spaces")
    private String city;

    @NotBlank(message = "state is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "State name must contain only letters and spaces")
    private String state;

    @NotBlank(message = "postalCode is required")
    @Pattern(regexp = "\\d{4,6}", message = "Postal code must be between 4 and 6 digits")
    private String postalCode;

    @Size(max = 200, message = "Additional information must be at most 200 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9 ,]*$", message = "Additional information can only contain letters, numbers, spaces, and commas")
    private String additionalInfo;
}
