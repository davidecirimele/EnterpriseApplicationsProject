package com.enterpriseapplicationsproject.ecommerce.exception;

public class ProductNotFoundException  extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
