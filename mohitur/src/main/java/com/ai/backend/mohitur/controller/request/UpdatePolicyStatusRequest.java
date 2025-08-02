package com.ai.backend.mohitur.controller.request;

import com.ai.backend.mohitur.domain.entity.PolicyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePolicyStatusRequest {

    @NotNull(message = "Policy status is required")
    private PolicyStatus status;

    private String reason; // Optional reason for status change
}