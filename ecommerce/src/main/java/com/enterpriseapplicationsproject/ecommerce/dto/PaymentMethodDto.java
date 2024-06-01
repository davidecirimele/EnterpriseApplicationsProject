package com.enterpriseapplicationsproject.ecommerce.dto;

import java.util.List;

public class PaymentMethodDto {

    private Long paymentMethodId;

    private UserDto user;

    private String paymentMethodType;

    private String provider;

    private String cardNumber;

    private String expirationDate;

    private  String paypal;

    private List<TransactionDto> transactions;

    private List<OrderDto> orders;

}
