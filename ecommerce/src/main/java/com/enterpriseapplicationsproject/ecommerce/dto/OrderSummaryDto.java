package com.enterpriseapplicationsproject.ecommerce.dto;

import com.enterpriseapplicationsproject.ecommerce.data.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDate;


@Data
public class OrderSummaryDto {

    private Long orderId;

    private String email;

    private LocalDate OrderDate;

    private Double totalAmount;

    private OrderStatus orderStatus;


}
