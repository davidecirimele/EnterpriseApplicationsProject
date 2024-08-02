package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

@Data
public class EditAddressDto {

    private Long id;

    private UserIdDto userId;

    private String street;

    private String province;

    private String city;

    private String state;

    private String postalCode;

    private String additionalInfo;

}
