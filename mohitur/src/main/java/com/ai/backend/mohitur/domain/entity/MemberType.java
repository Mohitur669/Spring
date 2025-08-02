package com.ai.backend.mohitur.domain.entity;

import lombok.Getter;

@Getter
public enum MemberType {
    PRIMARY("Primary", "Primary policyholder", 1),
    SPOUSE("Spouse", "Spouse of primary policyholder", 1),
    DEPENDENT("Dependent", "Dependent family member", 10),
    BENEFICIARY("Beneficiary", "Insurance beneficiary", 5),
    CHILD("Child", "Child of primary policyholder", 10);

    private final String displayName;
    private final String description;
    private final int maxAllowed;

    MemberType(String displayName, String description, int maxAllowed) {
        this.displayName = displayName;
        this.description = description;
        this.maxAllowed = maxAllowed;
    }

    public boolean isPrimary() {
        return this == PRIMARY;
    }

    public boolean isDependent() {
        return this == DEPENDENT || this == CHILD;
    }

    public boolean requiresAgeValidation() {
        return this == CHILD;
    }
}