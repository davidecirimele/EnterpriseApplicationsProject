package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.validation.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SaveUserDto {

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @NotNull(message = "Birth Date is required")
    private LocalDate birthDate;

    @Valid
    @NotNull(message = "Credentials are required")
    private CredentialDto credential;

    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    private String phoneNumber;

    public CredentialDto getCredential(){
        if(credential==null)
            credential = new CredentialDto();
        return credential;
    }

}
