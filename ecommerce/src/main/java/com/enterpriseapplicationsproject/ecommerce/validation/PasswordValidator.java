package com.enterpriseapplicationsproject.ecommerce.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword,String>{

    private int minLength;
    private int maxLength;
    private String allowedSpecialCharacters;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.allowedSpecialCharacters = constraintAnnotation.allowedSpecialCharacters();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() < minLength || value.length() > maxLength) {
            return false;
        }
        String specialCharacterPattern = "[" + Pattern.quote(allowedSpecialCharacters) + "]";
        if (!value.matches(".*" + specialCharacterPattern + ".*")) {
            return false;
        }

        String nonAllowedSpecialCharacterPattern = "[^0-9a-zA-Z" + Pattern.quote(allowedSpecialCharacters) + "]";
        if (value.matches(".*" + nonAllowedSpecialCharacterPattern + ".*")) {
            return false;
        }
        return true;
    }
}
