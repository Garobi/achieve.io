package com.ddg.achieveio.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidResourceNameValidator implements ConstraintValidator<ValidResourceName, String> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidResourceName constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String trimmedValue = value.trim();

        if (trimmedValue.isEmpty()) {
            return false;
        }

        int length = trimmedValue.length();
        return length >= min && length <= max;
    }
}