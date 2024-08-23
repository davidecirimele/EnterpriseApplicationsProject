package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;


@EqualsAndHashCode()
@Entity
@Data
@NoArgsConstructor
public class Book{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    @Column(name = "COVER_URL")
    private String coverUrl;
    //TODO immagine

    @Column(name = "WEIGHT")
    private Double weight;

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
    @Column(name = "ISBN", unique = true)
    private String ISBN;
    @Column(name = "PAGES")
    private int pages;
    @Column(name = "EDITION")
    private String edition;

    @Column(name = "FORMAT")
    @Enumerated(EnumType.STRING)
    private BookFormat format;
    @Column(name = "GENRE")
    @Enumerated(EnumType.STRING)
    private BookGenre genre;
    @Column(name = "LANGUAGE")
    @Enumerated(EnumType.STRING)
    private BookLanguage language;

    @Column(name = "PUBLISHER")
    private String publisher;
    @Column(name = "AGE")
    private int age;
    @Column(name = "PUBLISH_DATE")
    private LocalDate publishDate;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

}