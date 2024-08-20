package com.enterpriseapplicationsproject.ecommerce.validation;

import jakarta.validation.Payload;

public @interface ValidPassword {

    String message() default "Invalid password";

    int minLength() default 8;

    int maxLength() default 20;

    String allowedSpecialCharacters() default "!@#^&*()-+";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
