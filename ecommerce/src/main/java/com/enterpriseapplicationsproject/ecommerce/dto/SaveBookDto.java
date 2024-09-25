package com.enterpriseapplicationsproject.ecommerce.dto;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Publish date is mandatory")
    private LocalDate publishDate;

    @PositiveOrZero(message = "Weight must be positive or zero")
    private Double weight;

    @Positive(message = "Price must be positive")
    private Double price;

    @PositiveOrZero(message = "Stock must be positive or zero")
    private Integer stock;

}
