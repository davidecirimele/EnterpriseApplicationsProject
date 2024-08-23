package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserDetailsDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

}
