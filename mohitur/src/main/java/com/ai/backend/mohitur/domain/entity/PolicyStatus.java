package com.ai.backend.mohitur.domain.entity;

import lombok.Getter;

@Getter
public enum PolicyStatus {
    ACTIVE("Active", "Policy is active and coverage is in effect"),
    INACTIVE("Inactive", "Policy is temporarily inactive"),
    EXPIRED("Expired", "Policy has reached its end date"),
    CANCELLED("Cancelled", "Policy has been cancelled by policyholder or insurer"),
    SUSPENDED("Suspended", "Policy is suspended due to non-payment or other reasons"),
    PENDING("Pending", "Policy is pending approval or payment");

    private final String displayName;
    private final String description;

    PolicyStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canBeModified() {
        return this == ACTIVE || this == SUSPENDED;
    }

    public boolean canAddMembers() {
        return this == ACTIVE;
    }
}