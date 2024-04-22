package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Book extends Product{

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