package com.enterpriseapplicationsproject.ecommerce.exception;

public class RevokingTokenErrorException extends RuntimeException {
    public RevokingTokenErrorException(String message) {
        super(message);
    }
}
