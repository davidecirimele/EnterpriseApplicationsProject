package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SaveUserDto {

    private String lastname;
    private String firstname;
    private LocalDate birthdate;
    private CredentialDto credential;
    private String phonenumber;

}
