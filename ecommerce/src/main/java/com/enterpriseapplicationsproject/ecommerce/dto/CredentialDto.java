package com.enterpriseapplicationsproject.ecommerce.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CredentialDto {

    private String email;
    @JsonIgnore
    private String password;

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

}
