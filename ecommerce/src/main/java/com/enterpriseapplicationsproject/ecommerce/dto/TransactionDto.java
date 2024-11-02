package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
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

    private PaymentStatus PaymentStatus;

    private LocalDate transactionDate;
}
