package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SaveUserDto {

    private UUID id;

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @NotBlank(message = "Birth Data is required")
    private LocalDate birthDate;

    @NotNull(message = "Credentials are required")
    private CredentialDto credential;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    public CredentialDto getCredentials(){
        if(credential==null)
            credential = new CredentialDto();
        return credential;
    }

}
