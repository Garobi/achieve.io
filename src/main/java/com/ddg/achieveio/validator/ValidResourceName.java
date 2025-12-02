package com.ddg.achieveio.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidResourceNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidResourceName {

    String message() default "O nome não pode ser vazio e deve ter entre {min} e {max} caracteres.";

    int min() default 3;

    int max() default 100;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}