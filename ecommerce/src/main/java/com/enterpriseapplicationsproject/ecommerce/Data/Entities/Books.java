package com.enterpriseapplicationsproject.ecommerce.Data.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Entity
public class Books extends Product{

    @Id
    private long bookId;
    //private String category;
    //private String weight;
    //private Date insertDate;
    //private float price;
    //private String description;
    //private int stock;
    private String title;
    private String author;
    private String ISBN;
    private int pages;
    private String edition;
    private String format;
    private String genre;
    private String language;
    private String publisher;
    private int age;
    private Date publishDate;

}
