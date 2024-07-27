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

    private String profilePicture;

    private CredentialDto credential;

    private List<Address> addresses;

    private String phoneNumber;

    private List<Group> groups;

}
