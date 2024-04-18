package com.enterpriseapplicationsproject.ecommerce.Data.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

@Entity
@Data
public class User {


    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private ImageIcon proPic;
    private String email;
    private String password;
    private String defaultAddress;
    private String phoneNumber;
    private String role;

}
