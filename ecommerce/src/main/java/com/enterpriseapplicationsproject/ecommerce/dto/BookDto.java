package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.BookFormat;
import com.enterpriseapplicationsproject.ecommerce.data.entities.BookGenre;
import com.enterpriseapplicationsproject.ecommerce.data.entities.BookLanguage;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class BookDto {

    private Long id;

    private String title;

    private String author;

    private String ISBN;

    private int pages;

    private String edition;

    private BookFormat format;

    private LocalDate insertDate;

    private BookGenre genre;

    private BookLanguage language;

    private String publisher;

    private int age;

    private Date publishDate;

    private String category;

    private Double weight;

    private Double price;

    private Integer stock;

}
