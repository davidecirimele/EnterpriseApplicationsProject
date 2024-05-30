package com.enterpriseapplicationsproject.ecommerce.dto;

import java.time.LocalDate;

public class OrderDto {

    private Long orderId;

    private UserDto user;

    private AddressDto address;

    private LocalDate date;

    private double totalAmount;

    private String orderStatus;

    //private PaymentMethodDto paymentMethod;

    //private List<OrderItemDto> orderItems;
}
