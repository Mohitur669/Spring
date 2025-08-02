package com.ai.backend.mohitur.repository;

import com.ai.backend.mohitur.domain.entity.Member;
import com.ai.backend.mohitur.domain.entity.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.policy.id = :policyId")
    List<Member> findByPolicyId(@Param("policyId") Long policyId);

    @Query("SELECT m FROM Member m WHERE m.contactInfo.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("SELECT m FROM Member m WHERE m.memberType = :type")
    List<Member> findByMemberType(@Param("type") MemberType type);
}