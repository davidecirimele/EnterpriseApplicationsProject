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

}
