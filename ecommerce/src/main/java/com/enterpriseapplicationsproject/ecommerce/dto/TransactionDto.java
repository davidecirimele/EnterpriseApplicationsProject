package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.time.LocalDate;

@Data
public class TransactionDto {

    private Long id;

    private UserIdDto user;

    private OrderIdDto order;

    private BasicPaymentMethodDto paymentMethod;

    private Double amount;

    private String status;

    private LocalDate date;
}
