package com.ddg.achieveio.validator; // Ajuste o pacote conforme sua estrutura

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Set;
import java.util.UUID;

@Documented
@Constraint(validatedBy = NotEmptyUUIDListValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyUUIDList {

    String message() default "A lista de IDs não pode ser vazia.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}