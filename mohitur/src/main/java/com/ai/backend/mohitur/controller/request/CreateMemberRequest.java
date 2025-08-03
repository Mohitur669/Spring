package com.ai.backend.mohitur.controller.request;

import com.ai.backend.mohitur.domain.entity.MemberType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {

    @NotNull(message = "Policy ID is required")
    private Long policyId;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name can only contain letters and spaces")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name can only contain letters and spaces")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be 10-15 digits, optionally starting with +")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Member type is required")
    private MemberType memberType;

    // Optional fields
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @Size(max = 100, message = "Occupation cannot exceed 100 characters")
    private String occupation;

    @Size(max = 15, message = "Emergency contact cannot exceed 15 characters")
    @Pattern(regexp = "^[+]?[0-9]*$", message = "Emergency contact must contain only digits and optional +")
    private String emergencyContact;

    @AssertTrue(message = "Member must be at least 18 years old")
    public boolean isAgeValid() {
        return dateOfBirth != null && dateOfBirth.isBefore(LocalDate.now().minusYears(18));
    }
}