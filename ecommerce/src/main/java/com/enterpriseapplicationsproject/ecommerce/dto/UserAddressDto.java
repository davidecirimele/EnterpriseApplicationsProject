package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserAddressDto {

    private UUID id;

    private List<AddressDto> addresses;

}
