package com.enterpriseapplicationsproject.ecommerce.data.entities;

import com.enterpriseapplicationsproject.ecommerce.data.domain.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
public class Credential {

    @Column(name="EMAIL", unique = true)
    private String email;

    @Column(name="PASSWORD")
    private String password;

}
