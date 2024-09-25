package com.enterpriseapplicationsproject.ecommerce.validation;

import jakarta.validation.Payload;

public @interface ValidPhoneNumber {

    String message() default "Invalid phone number";

    int minLength() default 6;

    int maxLength() default 15;

    String allowedCharacters() default "0-9";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
