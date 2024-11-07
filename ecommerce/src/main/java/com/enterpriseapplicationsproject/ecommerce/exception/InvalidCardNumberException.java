package com.enterpriseapplicationsproject.ecommerce.exception;

public class InvalidCardNumberException extends RuntimeException {
    public InvalidCardNumberException(String message) {
        super(message);
    }
}
