package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.time.LocalDate;

@Data
public class TransactionDto {

    private Long id;

    private UserDto user;

    private OrderDto order;

    private PaymentMethodDto paymentMethod;

    private double amount;

    private String status;

    private LocalDate date;
}
