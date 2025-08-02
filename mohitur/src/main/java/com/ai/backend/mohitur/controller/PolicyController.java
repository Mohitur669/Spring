package com.ai.backend.mohitur.controller;

import com.ai.backend.mohitur.controller.request.CreatePolicyRequest;
import com.ai.backend.mohitur.controller.request.UpdatePolicyStatusRequest;
import com.ai.backend.mohitur.controller.response.ApiResponse;
import com.ai.backend.mohitur.domain.entity.Policy;
import com.ai.backend.mohitur.service.PolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@Slf4j
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Policy>> createPolicy(
            @Valid @RequestBody CreatePolicyRequest request) {

        Policy policy = policyService.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Policy created successfully", policy));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Policy>>> getAllPolicies() {
        List<Policy> policies = policyService.findAllPolicies();
        return ResponseEntity.ok(
                ApiResponse.success("Policies retrieved successfully", policies));
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<ApiResponse<Policy>> getPolicyByNumber(
            @PathVariable String policyNumber) {

        return policyService.findByPolicyNumber(policyNumber)
                .map(policy -> ResponseEntity.ok(
                        ApiResponse.success("Policy found", policy)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{policyId}/status")
    public ResponseEntity<ApiResponse<Policy>> updatePolicyStatus(
            @PathVariable Long policyId,
            @Valid @RequestBody UpdatePolicyStatusRequest request) {

        Policy policy = policyService.updatePolicyStatus(policyId, request.getStatus());
        return ResponseEntity.ok(
                ApiResponse.success("Policy status updated successfully", policy));
    }

    @DeleteMapping("/{policyId}")
    public ResponseEntity<ApiResponse<Void>> deletePolicy(@PathVariable Long policyId) {
        policyService.deletePolicy(policyId);
        return ResponseEntity.ok(
                ApiResponse.success("Policy deleted successfully", null));
    }
}