package com.ai.backend.mohitur.domain.entity;

import com.ai.backend.mohitur.domain.valueObject.ContactInfo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// REMOVE THIS: import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
public class Member {
    @Id  // This is now jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private String occupation;

    @Embedded
    private ContactInfo contactInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType memberType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Member(String firstName, String lastName, String email, String phoneNumber,
                  LocalDate dateOfBirth, MemberType memberType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = ContactInfo.of(email, phoneNumber);
        this.memberType = memberType;
    }

    // Keep the existing constructor too
    public Member(String firstName, String lastName, LocalDate dateOfBirth,
                  ContactInfo contactInfo, MemberType memberType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
        this.memberType = memberType;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Delegate methods for easier access
    public String getEmail() {
        return contactInfo != null ? contactInfo.getEmail() : null;
    }

    public String getPhoneNumber() {
        return contactInfo != null ? contactInfo.getPhoneNumber() : null;
    }
}