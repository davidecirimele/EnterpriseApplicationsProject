package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class SaveBookDto {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;

    @NotBlank(message = "ISBN is mandatory")
    @Pattern(regexp = "^(97(8|9))?[0-9]{9}[0-9X]$", message = "ISBN is not valid")
    private String ISBN;

    @Positive(message = "Pages must be positive")
    private int pages;

    @NotBlank(message = "Edition is mandatory")
    private String edition;

    @NotBlank(message = "Format is mandatory")
    private String format;

    @NotBlank(message = "Genre is mandatory")
    private String genre;

    @NotBlank(message = "Language is mandatory")
    private String language;

    @NotBlank(message = "Publisher is mandatory")
    private String publisher;

    @Positive(message = "Age must be positive")
    private int age;

    @NotBlank(message = "Publish date is mandatory")
    private Date publishDate;

    @NotBlank(message = "Category is mandatory")
    private String category;

    @Positive(message = "Weight must be positive")
    private Double weight;

    @Positive(message = "Price must be positive")
    private Double price;

    @Positive(message = "Stock must be positive")
    private Integer stock;

}
