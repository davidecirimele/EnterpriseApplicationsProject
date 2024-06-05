package com.enterpriseapplicationsproject.ecommerce.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class OrderDto {

    private Long orderId;

    private UserIdDto user;

    private AddressIdDto address;

    private LocalDate date;

    private double totalAmount;

    private String orderStatus;

    private PaymentMethodIdDto paymentMethod;

    private List<OrderItemDto> orderItems;


}
