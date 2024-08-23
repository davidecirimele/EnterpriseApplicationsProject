package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

@Data
public class BookCartDto {

    private Long id;

    private String title;

    private String author;

    private Double price;
}
