package com.ai.backend.mohitur.controller;

import com.ai.backend.mohitur.controller.request.CreateMemberRequest;
import com.ai.backend.mohitur.controller.request.CreateMultipleMembersRequest;
import com.ai.backend.mohitur.controller.request.UpdateMemberRequest;
import com.ai.backend.mohitur.controller.response.ApiResponse;
import com.ai.backend.mohitur.domain.entity.Member;
import com.ai.backend.mohitur.exception.BusinessException;
import com.ai.backend.mohitur.exception.ValidationException;
import com.ai.backend.mohitur.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/addMemberToPolicy")
    public ResponseEntity<ApiResponse<Member>> addMemberToPolicy(@Valid @RequestBody CreateMemberRequest request) {

        log.info("Adding member to policy with ID: {}", request.getPolicyId());
        try {
            Member member = memberService.addMemberToPolicy(request.getPolicyId(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Member added successfully", member));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/addMultipleMembersToPolicy")
    public ResponseEntity<ApiResponse<List<Member>>> addMultipleMembersToPolicy(@Valid @RequestBody CreateMultipleMembersRequest request) {

        log.info("Adding {} members to policy with ID: {}", request.getMembers().size(), request.getPolicyId());

        if (request.getMembers() == null || request.getMembers().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("At least one member is required"));
        }

        try {
            List<Member> addedMembers = memberService.addMultipleMembersToPolicy(request.getPolicyId(), request.getMembers());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(String.format("Successfully added %d members to policy", addedMembers.size()), addedMembers));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/getMembersByPolicy")
    public ResponseEntity<ApiResponse<List<Member>>> getMembersByPolicy(@Valid @RequestParam("policyId") Long policyId) {

        log.info("Fetching members for policy ID: {}", policyId);

        if (null == policyId) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Policy ID is required"));
        }

        try {
            List<Member> members = memberService.findMembersByPolicyId(policyId);
            return ResponseEntity.ok(ApiResponse.success("Members retrieved successfully", members));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/getMemberById")
    public ResponseEntity<ApiResponse<Member>> getMemberById(@Valid @RequestParam("memberId") Long memberId) {

        log.info("Fetching member with ID: {}", memberId);

        if (null == memberId) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Member ID is required"));
        }

        try {
            Member member = memberService.findMemberById(memberId);
            return ResponseEntity.ok(ApiResponse.success("Member found", member));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/updateMember")
    public ResponseEntity<ApiResponse<Member>> updateMember(@Valid @RequestBody UpdateMemberRequest request) {

        log.info("Updating member with ID: {}", request.getMemberId());

        if (null == request.getMemberId()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Member ID is required"));
        }

        try {
            Member member = memberService.updateMember(request.getMemberId(), request);
            return ResponseEntity.ok(ApiResponse.success("Member updated successfully", member));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/removeMember")
    public ResponseEntity<ApiResponse<Void>> removeMember(@Valid @RequestParam("policyNumber") String policyNumber, @Valid @RequestParam("memberId") Long memberId) {

        log.info("Removing member with ID: {}", memberId);

        if (null == memberId && !policyNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Member ID is required"));
        }

        try {
            memberService.removeMember(memberId);
            return ResponseEntity.ok(ApiResponse.success("Member removed successfully", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/searchMembersByEmailAndMemberType")
    public ResponseEntity<ApiResponse<List<Member>>> searchMembersByEmailAndMemberType(@Valid @RequestParam("email") String email, @Valid @RequestParam("memberType") String memberType) {

        log.info("Searching members with email: {}, type: {}", email, memberType);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email is required"));
        }

        try {
            List<Member> members = memberService.searchMembers(email, memberType);
            return ResponseEntity.ok(ApiResponse.success("Search completed successfully", members));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}