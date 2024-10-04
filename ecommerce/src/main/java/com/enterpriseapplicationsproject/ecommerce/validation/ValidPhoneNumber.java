package com.enterpriseapplicationsproject.ecommerce.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {

    String message() default "Invalid phone number";

    int minLength() default 6;

    int maxLength() default 15;

    String allowedCharacters() default "0-9";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
