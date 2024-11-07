package com.enterpriseapplicationsproject.ecommerce.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;


@EqualsAndHashCode()
@Entity
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "image_path")
    private String imagePath;

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

    @Column(name = "ISBN", unique = true)
    private String ISBN;

    @Column(name = "PAGES")
    private int pages;

    @Column(name = "EDITION")
    private String edition;

    @Column(name = "FORMAT")
    private BookFormat format;

    @Column(name = "GENRE")
    private BookGenre genre;

    @Column(name = "LANGUAGE")
    private BookLanguage language;

    @Column(name = "PUBLISHER")
    private String publisher;

    @Column(name = "AGE")
    private int age;

    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;
}
