package com.enterpriseapplicationsproject.ecommerce.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private int minLength;
    private int maxLength;
    private String allowedCharacters;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.allowedCharacters = constraintAnnotation.allowedCharacters();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() < minLength || value.length() > maxLength) {
            return false;
        }

        String allowedPattern = "[" + Pattern.quote(allowedCharacters) + "]";
        String regex = "^[0-9" + Pattern.quote(allowedCharacters) + "]+$";

        return value.matches(regex);
    }
}
