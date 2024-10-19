package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.entities.BookFormat;
import com.enterpriseapplicationsproject.ecommerce.data.entities.BookGenre;
import com.enterpriseapplicationsproject.ecommerce.data.entities.BookLanguage;
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

    @NotNull(message = "Format is mandatory")
    private BookFormat format;

    @NotNull(message = "Genre is mandatory")
    private BookGenre genre;

    @NotNull(message = "Language is mandatory")
    private BookLanguage language;

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
