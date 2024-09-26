package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.BookFormat;
import com.enterpriseapplicationsproject.ecommerce.data.entities.BookGenre;
import com.enterpriseapplicationsproject.ecommerce.data.entities.BookLanguage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile image;
}
