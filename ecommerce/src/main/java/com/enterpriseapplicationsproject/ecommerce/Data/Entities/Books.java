package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Entity
public class Books extends Product{

    @Id
    @Column(name = "BOOK_ID")
    private long bookId;
    //private String category;
    //private String weight;
    //private Date insertDate;
    //private float price;
    //private String description;
    //private int stock;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "ISBN")
    private String ISBN;
    @Column(name = "PAGES")
    private int pages;
    @Column(name = "EDITION")
    private String edition;
    @Column(name = "FORMAT")
    private String format;
    @Column(name = "GENRE")
    private String genre;
    @Column(name = "LANGUAGE")
    private String language;
    @Column(name = "PUBLISHER")
    private String publisher;
    @Column(name = "AGE")
    private int age;
    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

}
