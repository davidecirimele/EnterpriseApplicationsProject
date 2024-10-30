package com.enterpriseapplicationsproject.ecommerce.exception;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String message) {
        super(message);
    }
}
