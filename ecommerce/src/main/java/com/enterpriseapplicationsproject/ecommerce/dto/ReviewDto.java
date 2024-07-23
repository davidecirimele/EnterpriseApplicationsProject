package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long id;

    private String author;

    private String title;

    private String description;

    private LocalDateTime reviewDate;

    private Long productId;

}
