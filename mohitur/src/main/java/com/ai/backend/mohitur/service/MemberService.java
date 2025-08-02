package com.ai.backend.mohitur.service;

import com.ai.backend.mohitur.controller.request.CreateMemberRequest;
import com.ai.backend.mohitur.controller.request.UpdateMemberRequest;
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

import java.util.List;

@Service
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PolicyRepository policyRepository;

    public MemberService(MemberRepository memberRepository,
                         PolicyRepository policyRepository) {
        this.memberRepository = memberRepository;
        this.policyRepository = policyRepository;
    }

    public Member addMemberToPolicy(Long policyId, CreateMemberRequest request) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Policy not found with id: " + policyId));

        // Check for duplicate email
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Member with email already exists: " + request.getEmail());
        }

        Member member = new Member(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getDateOfBirth(),
                request.getMemberType()
        );

        // Update optional fields through ContactInfo
        if (request.getAddress() != null || request.getEmergencyContact() != null) {
            ContactInfo updatedContactInfo = ContactInfo.of(
                    request.getEmail(),
                    request.getPhoneNumber(),
                    request.getAddress(),
                    request.getEmergencyContact()
            );
            member.setContactInfo(updatedContactInfo);
        }

        member.setOccupation(request.getOccupation());

        policy.addMember(member);
        Member savedMember = memberRepository.save(member);

        log.info("Member added to policy {}: {} {}",
                policy.getPolicyNumberValue(), member.getFirstName(), member.getLastName());

        return savedMember;
    }

    @Transactional(readOnly = true)
    public List<Member> findMembersByPolicyId(Long policyId) {
        return memberRepository.findByPolicyId(policyId);
    }

    public Member updateMember(Long memberId, UpdateMemberRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("Member not found with id: " + memberId));

        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setOccupation(request.getOccupation());

        // Update ContactInfo with new values
        ContactInfo updatedContactInfo = ContactInfo.of(
                member.getEmail(), // Keep existing email
                request.getPhoneNumber(),
                request.getAddress(),
                request.getEmergencyContact()
        );
        member.setContactInfo(updatedContactInfo);

        return memberRepository.save(member);
    }

    public void removeMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new BusinessException("Member not found with id: " + memberId);
        }
        memberRepository.deleteById(memberId);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
    }

    public List<Member> searchMembers(String email, String memberType) {
        if (email != null && !email.isEmpty()) {
            return memberRepository.findByEmail(email)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (memberType != null && !memberType.isEmpty()) {
            try {
                MemberType type = MemberType.valueOf(memberType.toUpperCase());
                return memberRepository.findByMemberType(type);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid member type: " + memberType);
            }
        }

        return memberRepository.findAll();
    }
}