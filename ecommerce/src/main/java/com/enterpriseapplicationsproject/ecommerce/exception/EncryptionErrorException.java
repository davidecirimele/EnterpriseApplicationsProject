package com.enterpriseapplicationsproject.ecommerce.exception;

public class EncryptionErrorException extends RuntimeException {
    public EncryptionErrorException(String message) {
        super(message);
    }
}
