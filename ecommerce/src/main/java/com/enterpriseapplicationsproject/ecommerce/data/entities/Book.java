package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Book extends Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "CATEGORY")
    private String category;

    //TODO immagine

    @Column(name = "WEIGHT")
    private Double weight;

    @Basic(optional = false)
    @Column(name = "INSERT_DATE")
    private LocalDate insertDate;

    @Basic(optional = false)
    @Column(name = "PRICE")
    private Double price;

    @Basic(optional = false)
    @Column(name = "STOCK")
    private Integer stock;


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

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

}