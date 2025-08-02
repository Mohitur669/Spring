package com.ai.backend.mohitur.service;

import com.ai.backend.mohitur.controller.request.CreatePolicyRequest;
import com.ai.backend.mohitur.domain.entity.Policy;
import com.ai.backend.mohitur.domain.entity.PolicyStatus;
import com.ai.backend.mohitur.exception.BusinessException;
import com.ai.backend.mohitur.repository.MemberRepository;
import com.ai.backend.mohitur.repository.PolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final MemberRepository memberRepository;

    public PolicyService(PolicyRepository policyRepository,
                         MemberRepository memberRepository) {
        this.policyRepository = policyRepository;
        this.memberRepository = memberRepository;
    }

    public Policy createPolicy(CreatePolicyRequest request) {
        log.info("Creating policy with type: {}", request.getPolicyType());

        Policy policy = new Policy(
                request.getPolicyType(),
                request.getPremiumAmount(),
                request.getStartDate(),
                request.getEndDate()
        );

        Policy savedPolicy = policyRepository.save(policy);
        log.info("Policy created with number: {}", savedPolicy.getPolicyNumber());

        return savedPolicy;
    }

    @Transactional(readOnly = true)
    public Optional<Policy> findByPolicyNumber(String policyNumber) {
        return policyRepository.findByPolicyNumber(policyNumber);
    }

    @Transactional(readOnly = true)
    public List<Policy> findAllPolicies() {
        return policyRepository.findAll();
    }

    public Policy updatePolicyStatus(Long policyId, PolicyStatus status) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Policy not found with id: " + policyId));

        policy.setStatus(status);
        return policyRepository.save(policy);
    }

    public void deletePolicy(Long policyId) {
        if (!policyRepository.existsById(policyId)) {
            throw new BusinessException("Policy not found with id: " + policyId);
        }
        policyRepository.deleteById(policyId);
    }

    @Scheduled(cron = "0 0 1 * * ?") // Daily at 1 AM
    public void updateExpiredPolicies() {
        List<Policy> expiredPolicies = policyRepository.findExpiredPolicies(LocalDate.now());
        expiredPolicies.forEach(policy -> {
            policy.setStatus(PolicyStatus.EXPIRED);
            policyRepository.save(policy);
        });
        log.info("Updated {} expired policies", expiredPolicies.size());
    }
}