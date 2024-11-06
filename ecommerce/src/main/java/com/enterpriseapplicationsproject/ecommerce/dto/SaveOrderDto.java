package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


    @Data
    public class SaveOrderDto {

        @NotNull(message = "The order id is required")
        private Long id;

        @NotNull(message = "The user id is required")
        @Valid
        private UserIdDto user;

        @NotNull(message = "The address is required")
        @Valid
        private AddressDto address;

        @FutureOrPresent(message = "The date must be in the future or present")
        private LocalDate date;

        @PositiveOrZero(message = "The total amount must be positive")
        private Double totalAmount;

        @Enumerated( jakarta.persistence.EnumType.STRING)
        private OrderStatus orderStatus;

        @NotNull(message = "The payment method is required")
        @Valid
        private PaymentMethodIdDto paymentMethod;

        @NotNull(message = "The order items are required")
        @Size(min = 1, message = "The order must have at least one item")
        @Valid
        private List<SaveOrderItemDto> orderItems;


    }

