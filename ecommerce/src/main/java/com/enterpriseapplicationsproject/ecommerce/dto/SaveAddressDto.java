package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveAddressDto {

    private UserIdDto user;

    @NotBlank(message = "street is required")
    private String street;

    @NotBlank(message = "province is required")
    private String province;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "state is required")
    private String state;

    @NotBlank(message = "postalCode is required")
    private String postalCode;

    private String additionalInfo;
}
