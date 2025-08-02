package com.ai.backend.mohitur.controller;


import com.ai.backend.mohitur.controller.request.CreateMemberRequest;
import com.ai.backend.mohitur.controller.request.UpdateMemberRequest;
import com.ai.backend.mohitur.controller.response.ApiResponse;
import com.ai.backend.mohitur.domain.entity.Member;
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

    @PostMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<Member>> addMemberToPolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody CreateMemberRequest request) {

        log.info("Adding member to policy with ID: {}", policyId);
        Member member = memberService.addMemberToPolicy(policyId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member added successfully", member));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<ApiResponse<List<Member>>> getMembersByPolicy(
            @PathVariable Long policyId) {

        log.info("Fetching members for policy ID: {}", policyId);
        List<Member> members = memberService.findMembersByPolicyId(policyId);

        return ResponseEntity.ok(
                ApiResponse.success("Members retrieved successfully", members));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Member>> getMemberById(
            @PathVariable Long memberId) {

        log.info("Fetching member with ID: {}", memberId);
        Member member = memberService.findMemberById(memberId);

        return ResponseEntity.ok(
                ApiResponse.success("Member found", member));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Member>> updateMember(
            @PathVariable Long memberId,
            @Valid @RequestBody UpdateMemberRequest request) {

        log.info("Updating member with ID: {}", memberId);
        Member member = memberService.updateMember(memberId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Member updated successfully", member));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long memberId) {

        log.info("Removing member with ID: {}", memberId);
        memberService.removeMember(memberId);

        return ResponseEntity.ok(
                ApiResponse.success("Member removed successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Member>>> searchMembers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String memberType) {

        log.info("Searching members with email: {}, type: {}", email, memberType);
        List<Member> members = memberService.searchMembers(email, memberType);

        return ResponseEntity.ok(
                ApiResponse.success("Search completed successfully", members));
    }
}