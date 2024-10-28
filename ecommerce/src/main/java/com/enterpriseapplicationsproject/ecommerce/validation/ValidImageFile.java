package com.enterpriseapplicationsproject.ecommerce.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
public @interface ValidImageFile {
    String message() default "File not valid or too large (max 5 MB, only JPEG or PNG)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
