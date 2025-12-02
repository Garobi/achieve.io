package com.ddg.achieveio.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.UUID;

public class NotEmptyUUIDListValidator implements ConstraintValidator<NotEmptyUUIDList, Set<UUID>> {

    @Override
    public boolean isValid(Set<UUID> idList, ConstraintValidatorContext context) {
        if (idList == null) {
            return true;
        }
        return !idList.isEmpty();
    }
}