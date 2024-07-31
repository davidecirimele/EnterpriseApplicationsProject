package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Group;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Birth Date is required")
    private LocalDate birthDate;
    private CredentialDto credentials;

    private List<AddressDto> addresses;

    private String phoneNumber;

    private List<Group> groups;

}
