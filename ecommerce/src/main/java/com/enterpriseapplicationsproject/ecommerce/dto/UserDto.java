package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String fullName;

    private LocalDate birthDate;

    private byte[] profilepPicture;

    private CredentialDto credential;

    private List<AddressDto> addresses;

    private String phoneNumber;

    private List<Group> groups;

}
