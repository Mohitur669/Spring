package com.ai.backend.mohitur.domain.valueObject;

import com.ai.backend.mohitur.utils.PolicyNumberGenerator;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class PolicyNumber {
    private String value;

    protected PolicyNumber() {} // JPA requirement

    private PolicyNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy number cannot be null or empty");
        }
        if (!isValidFormat(value)) {
            throw new IllegalArgumentException("Invalid policy number format");
        }
        this.value = value.trim();
    }

    public static PolicyNumber of(String value) {
        return new PolicyNumber(value);
    }

    public static PolicyNumber generate() {
        return new PolicyNumber(PolicyNumberGenerator.generate());
    }

    private boolean isValidFormat(String value) {
        return value.matches("^POL-\\d+-\\d{6}$");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PolicyNumber that = (PolicyNumber) obj;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}