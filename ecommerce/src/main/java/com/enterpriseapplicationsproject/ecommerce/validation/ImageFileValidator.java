package com.enterpriseapplicationsproject.ecommerce.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {

@Override
public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) return false;

        if (file.getSize() > 5 * 1024 * 1024) {
            return false;
        }

        String contentType = file.getContentType();
            return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }
}
