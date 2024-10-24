package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


    @Data
    public class SaveOrderDto {

        private Long id;

        private UserIdDto user;

        private AddressDto address;

        @FutureOrPresent(message = "The date must be in the future or present")
        private LocalDate date;

        @PositiveOrZero(message = "The total amount must be positive")
        private Double totalAmount;

        @Enumerated( jakarta.persistence.EnumType.STRING)
        private OrderStatus orderStatus;

        private PaymentMethodIdDto paymentMethod;

        private List<SaveOrderItemDto> orderItems;


    }

