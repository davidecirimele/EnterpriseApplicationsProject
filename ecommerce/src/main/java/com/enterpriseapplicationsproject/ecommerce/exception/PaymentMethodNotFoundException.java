package com.enterpriseapplicationsproject.ecommerce.exception;

public class PaymentMethodNotFoundException extends RuntimeException {
    public PaymentMethodNotFoundException(String message) {
        super(message);
    }
}
