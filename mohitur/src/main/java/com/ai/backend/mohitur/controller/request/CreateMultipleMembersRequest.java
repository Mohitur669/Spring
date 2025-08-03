package com.ai.backend.mohitur.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMultipleMembersRequest {

    @NotNull(message = "Policy ID is required")
    private Long policyId;

    @NotEmpty(message = "At least one member is required")
    @Size(min = 1, max = 50, message = "You can add between 1 and 50 members at once")
    @Valid
    private List<MemberDetailsRequest> members;
}