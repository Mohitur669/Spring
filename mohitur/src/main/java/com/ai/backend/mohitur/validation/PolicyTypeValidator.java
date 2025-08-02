package com.ai.backend.mohitur.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PolicyTypeValidator implements ConstraintValidator<ValidPolicyType, String> {

    private static final List<String> VALID_POLICY_TYPES = Arrays.asList(
            "LIFE", "HEALTH", "AUTO", "HOME", "TRAVEL", "BUSINESS"
    );

    @Override
    public void initialize(ValidPolicyType constraintAnnotation) {
        // Initialization if needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_POLICY_TYPES.contains(value.toUpperCase());
    }
}