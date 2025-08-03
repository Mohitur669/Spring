package com.ai.backend.mohitur.service;

import com.ai.backend.mohitur.controller.request.CreateMemberRequest;
import com.ai.backend.mohitur.controller.request.UpdateMemberRequest;
import com.ai.backend.mohitur.controller.request.MemberDetailsRequest;
import com.ai.backend.mohitur.domain.entity.Member;
import com.ai.backend.mohitur.domain.entity.MemberType;
import com.ai.backend.mohitur.domain.entity.Policy;
import com.ai.backend.mohitur.domain.valueObject.ContactInfo;
import com.ai.backend.mohitur.exception.BusinessException;
import com.ai.backend.mohitur.exception.ResourceNotFoundException;
import com.ai.backend.mohitur.exception.ValidationException;
import com.ai.backend.mohitur.repository.MemberRepository;
import com.ai.backend.mohitur.repository.PolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PolicyRepository policyRepository;

    public MemberService(MemberRepository memberRepository, PolicyRepository policyRepository) {
        this.memberRepository = memberRepository;
        this.policyRepository = policyRepository;
    }

    /**
     * Adds a single member to a policy
     */
    public Member addMemberToPolicy(Long policyId, CreateMemberRequest request) {
        log.info("Adding member to policy with ID: {}", policyId);

        Policy policy = policyRepository.findById(policyId).orElseThrow(() -> new BusinessException("Policy not found with id: " + policyId));

        // Check if policy allows adding members
        if (!policy.canAddMembers()) {
            throw new BusinessException("Cannot add members to policy with status: " + policy.getStatus());
        }

        // Check for duplicate email
        if (memberRepository.findByContactInfoEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Member with email already exists: " + request.getEmail());
        }

        // Validate member type constraints
        validateMemberTypeConstraints(policy, request.getMemberType());

        // Create ContactInfo value object
        ContactInfo contactInfo = ContactInfo.of(
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getEmergencyContact()
        );

        // Create Member entity
        Member member = new Member(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                contactInfo,
                request.getMemberType()
        );

        member.setOccupation(request.getOccupation());

        // Add member to policy
        policy.addMember(member);
        Member savedMember = memberRepository.save(member);

        log.info("Member added to policy {}: {} {}", policy.getPolicyNumberValue(), member.getFirstName(), member.getLastName());

        return savedMember;
    }

    /**
     * Adds multiple members to a policy in a single transaction
     */
    @Transactional
    public List<Member> addMultipleMembersToPolicy(Long policyId, List<MemberDetailsRequest> memberRequests) {
        log.info("Adding {} members to policy with ID: {}", memberRequests.size(), policyId);

        Policy policy = policyRepository.findById(policyId).orElseThrow(() -> new BusinessException("Policy not found with id: " + policyId));

        // Check if policy allows adding members
        if (!policy.canAddMembers()) {
            throw new BusinessException("Cannot add members to policy with status: " + policy.getStatus());
        }

        List<Member> addedMembers = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < memberRequests.size(); i++) {
            MemberDetailsRequest memberRequest = memberRequests.get(i);
            try {
                // Check for duplicate email
                if (memberRepository.findByContactInfoEmail(memberRequest.getEmail()).isPresent()) {
                    errors.add(String.format("Member %d: Email already exists - %s", i + 1, memberRequest.getEmail()));
                    continue;
                }

                // Validate member type constraints
                validateMemberTypeConstraints(policy, memberRequest.getMemberType());

                // Create ContactInfo value object
                ContactInfo contactInfo = ContactInfo.of(
                        memberRequest.getEmail(),
                        memberRequest.getPhoneNumber(),
                        memberRequest.getAddress(),
                        memberRequest.getEmergencyContact()
                );

                // Create Member entity
                Member member = new Member(
                        memberRequest.getFirstName(),
                        memberRequest.getLastName(),
                        memberRequest.getDateOfBirth(),
                        contactInfo,
                        memberRequest.getMemberType()
                );

                member.setOccupation(memberRequest.getOccupation());

                // Add member to policy
                policy.addMember(member);
                Member savedMember = memberRepository.save(member);
                addedMembers.add(savedMember);

                log.info("Member {} added to policy {}: {} {}",
                        i + 1, policy.getPolicyNumberValue(),
                        member.getFirstName(), member.getLastName());

            } catch (Exception e) {
                errors.add(String.format("Member %d (%s %s): %s",
                        i + 1, memberRequest.getFirstName(), memberRequest.getLastName(), e.getMessage()));
            }
        }

        // If there were any errors, throw an exception with details
        if (!errors.isEmpty()) {
            String errorMessage = String.format("Failed to add %d out of %d members. Errors: %s",
                    errors.size(), memberRequests.size(), String.join("; ", errors));
            throw new BusinessException(errorMessage);
        }

        log.info("Successfully added {} members to policy {}", addedMembers.size(), policy.getPolicyNumberValue());
        return addedMembers;
    }

    /**
     * Finds all members associated with a policy
     */
    @Transactional(readOnly = true)
    public List<Member> findMembersByPolicyId(Long policyId) {
        log.info("Finding members for policy ID: {}", policyId);

        if (policyId == null) {
            throw new ValidationException("Policy ID cannot be null");
        }

        // Verify policy exists
        if (!policyRepository.existsById(policyId)) {
            throw new BusinessException("Policy not found with id: " + policyId);
        }

        return memberRepository.findByPolicyId(policyId);
    }

    /**
     * Finds a member by ID
     */
    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        log.info("Finding member with ID: {}", memberId);

        if (memberId == null) {
            throw new ValidationException("Member ID cannot be null");
        }

        return memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
    }

    /**
     * Updates an existing member
     */
    public Member updateMember(Long memberId, UpdateMemberRequest request) {
        log.info("Updating member with ID: {}", memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException("Member not found with id: " + memberId));

        // Update basic fields
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setOccupation(request.getOccupation());

        // Update ContactInfo using the delegate method
        member.updateContactInfo(
                request.getPhoneNumber(),
                request.getAddress(),
                request.getEmergencyContact()
        );

        Member updatedMember = memberRepository.save(member);

        log.info("Member updated successfully: {} {}", member.getFirstName(), member.getLastName());
        return updatedMember;
    }

    /**
     * Removes a member from the system
     */
    public void removeMember(Long memberId) {
        log.info("Removing member with ID: {}", memberId);

        if (memberId == null) {
            throw new ValidationException("Member ID cannot be null");
        }

        if (!memberRepository.existsById(memberId)) {
            throw new BusinessException("Member not found with id: " + memberId);
        }

        memberRepository.deleteById(memberId);
        log.info("Member with ID {} removed successfully", memberId);
    }

    /**
     * Searches for members by email or member type
     */
    @Transactional(readOnly = true)
    public List<Member> searchMembers(String email, String memberType) {
        log.info("Searching members with email: {}, type: {}", email, memberType);

        if (email != null && !email.trim().isEmpty()) {
            return memberRepository.findByContactInfoEmail(email.trim())
                    .map(List::of)
                    .orElse(new ArrayList<>());
        }

        if (memberType != null && !memberType.trim().isEmpty()) {
            try {
                MemberType type = MemberType.valueOf(memberType.trim().toUpperCase());
                return memberRepository.findByMemberType(type);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid member type: " + memberType + ". Valid types are: " + String.join(", ", getMemberTypeValues()));
            }
        }

        // If both parameters are null or empty, return all members
        log.warn("No search criteria provided, returning all members");
        return memberRepository.findAll();
    }

    /**
     * Gets count of members by policy ID
     */
    @Transactional(readOnly = true)
    public long getMemberCountByPolicyId(Long policyId) {
        log.info("Getting member count for policy ID: {}", policyId);

        if (policyId == null) {
            throw new ValidationException("Policy ID cannot be null");
        }

        return memberRepository.countByPolicyId(policyId);
    }

    /**
     * Gets members by member type
     */
    @Transactional(readOnly = true)
    public List<Member> findMembersByType(MemberType memberType) {
        log.info("Finding members by type: {}", memberType);

        if (memberType == null) {
            throw new ValidationException("Member type cannot be null");
        }

        return memberRepository.findByMemberType(memberType);
    }

    /**
     * Checks if email is already taken
     */
    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return memberRepository.findByContactInfoEmail(email.trim()).isPresent();
    }

    // Private helper methods

    /**
     * Validates member type constraints for a policy
     */
    private void validateMemberTypeConstraints(Policy policy, MemberType memberType) {
        List<Member> existingMembers = policy.getMembers();

        // Check if adding this member type is allowed
        switch (memberType) {
            case PRIMARY:
                long primaryCount = existingMembers.stream().filter(m -> m.getMemberType() == MemberType.PRIMARY).count();

                if (primaryCount >= 1) {
                    throw new BusinessException("Policy can have only one PRIMARY member");
                }
                break;

            case SPOUSE:
                long spouseCount = existingMembers.stream().filter(m -> m.getMemberType() == MemberType.SPOUSE).count();

                if (spouseCount >= 1) {
                    throw new BusinessException("Policy can have only one SPOUSE member");
                }
                break;

            case CHILD:
            case DEPENDENT:
                long dependentCount = existingMembers.stream()
                        .filter(m -> m.getMemberType() == MemberType.CHILD || m.getMemberType() == MemberType.DEPENDENT)
                        .count();

                if (dependentCount >= 10) {
                    throw new BusinessException("Policy can have maximum 10 dependent members (children + dependents)");
                }
                break;

            case BENEFICIARY:
                long beneficiaryCount = existingMembers.stream().filter(m -> m.getMemberType() == MemberType.BENEFICIARY).count();

                if (beneficiaryCount >= 5) {
                    throw new BusinessException("Policy can have maximum 5 beneficiaries");
                }
                break;
        }
    }

    /**
     * Gets all valid member type values as strings
     */
    private List<String> getMemberTypeValues() {
        List<String> types = new ArrayList<>();
        for (MemberType type : MemberType.values()) {
            types.add(type.name());
        }
        return types;
    }
}