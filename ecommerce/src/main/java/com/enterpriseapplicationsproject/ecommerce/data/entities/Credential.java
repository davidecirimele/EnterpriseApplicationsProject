package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
public class Credential {

    @Column(name="EMAIL", unique = true)
    private String email;

    @Column(name="PASSWORD")
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
