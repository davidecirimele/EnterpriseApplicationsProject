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

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String phoneNumber;

}
