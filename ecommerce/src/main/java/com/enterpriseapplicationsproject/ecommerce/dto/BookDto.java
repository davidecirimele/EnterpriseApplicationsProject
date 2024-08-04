package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Date;

@Data
public class BookDto {

    private Long id;

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

    private String category;

    private Double weight;

    private Double price;

    private Integer stock;

}
