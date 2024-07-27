package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

public class SaveTransactionDto {


        private UserDto user;

        private OrderDto order;

        private PaymentMethodDto paymentMethod;

        @Positive(message = " Transaction Amount must be positive")
        private Double amount;


        @Enumerated( jakarta.persistence.EnumType.STRING)
        private PaymentStatus status;

        @FutureOrPresent(message = "Transaction Date must be in the present or future")
        private LocalDate date;
}

