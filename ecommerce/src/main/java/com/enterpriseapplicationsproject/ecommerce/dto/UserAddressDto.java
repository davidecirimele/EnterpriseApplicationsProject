package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserAddressDto {

    private Long id;

    private List<AddressDto> addresses;

}
