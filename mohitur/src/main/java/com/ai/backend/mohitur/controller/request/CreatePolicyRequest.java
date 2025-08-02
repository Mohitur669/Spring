package com.ai.backend.mohitur.controller.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePolicyRequest {

    @NotBlank(message = "Policy type is required")
    @Size(min = 2, max = 50, message = "Policy type must be between 2 and 50 characters")
    private String policyType;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "0.01", message = "Premium amount must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Premium amount cannot exceed 999,999.99")
    private BigDecimal premiumAmount;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @AssertTrue(message = "End date must be after start date")
    public boolean isEndDateValid() {
        return endDate != null && startDate != null && endDate.isAfter(startDate);
    }

    @AssertTrue(message = "Policy duration must be at least 1 year")
    public boolean isPolicyDurationValid() {
        return endDate != null && startDate != null &&
                endDate.isAfter(startDate.plusYears(1).minusDays(1));
    }
}