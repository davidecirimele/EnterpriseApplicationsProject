package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class OrderDto {

    private Long orderId;

    private UserIdDto user;

    private AddressDto address;

    private LocalDate date;

    private double totalAmount;

    private OrderStatus orderStatus;

    private PaymentMethodIdDto paymentMethod;

    private List<OrderItemWithoutIDDto> orderItems;


}
