package com.ai.backend.mohitur.repository;

import com.ai.backend.mohitur.domain.entity.Policy;
import com.ai.backend.mohitur.domain.entity.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    @Query("SELECT p FROM Policy p WHERE p.policyNumber.value = :policyNumber")
    Optional<Policy> findByPolicyNumberValue(@Param("policyNumber") String policyNumber);

    @Query("SELECT p FROM Policy p WHERE p.status = :status")
    List<Policy> findByStatus(@Param("status") PolicyStatus status);

    @Query("SELECT p FROM Policy p WHERE p.endDate < :date")
    List<Policy> findExpiredPolicies(@Param("date") LocalDate date);
}