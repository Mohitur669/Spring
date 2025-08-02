package com.ai.backend.mohitur.domain.entity;

import com.ai.backend.mohitur.domain.valueObject.PolicyNumber;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// REMOVE THIS: import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
@Data
@NoArgsConstructor
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "policy_number"))
    private PolicyNumber policyNumber;

    @Column(nullable = false)
    private String policyType;

    @Column(nullable = false)
    private BigDecimal premiumAmount;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Policy(String policyType, BigDecimal premiumAmount,
                  LocalDate startDate, LocalDate endDate) {
        this.policyNumber = PolicyNumber.generate();
        this.policyType = policyType;
        this.premiumAmount = premiumAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = PolicyStatus.ACTIVE;
    }

    public void addMember(Member member) {
        members.add(member);
        member.setPolicy(this);
    }

    public String getPolicyNumberValue() {
        return policyNumber != null ? policyNumber.getValue() : null;
    }
}