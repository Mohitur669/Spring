package com.ai.backend.mohitur.domain.valueObject;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class ContactInfo {
    // Getters
    private String email;
    private String phoneNumber;
    private String address;
    private String emergencyContact;

    protected ContactInfo() {} // JPA requirement

    private ContactInfo(String email, String phoneNumber, String address, String emergencyContact) {
        this.email = validateEmail(email);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.address = address;
        this.emergencyContact = emergencyContact;
    }

    public static ContactInfo of(String email, String phoneNumber, String address, String emergencyContact) {
        return new ContactInfo(email, phoneNumber, address, emergencyContact);
    }

    public static ContactInfo of(String email, String phoneNumber) {
        return new ContactInfo(email, phoneNumber, null, null);
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email.trim().toLowerCase();
    }

    private String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        String cleanPhone = phoneNumber.replaceAll("[^+\\d]", "");
        if (!cleanPhone.matches("^[+]?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        return cleanPhone;
    }

    // Business methods
    public ContactInfo updateAddress(String newAddress) {
        return new ContactInfo(this.email, this.phoneNumber, newAddress, this.emergencyContact);
    }

    public ContactInfo updateEmergencyContact(String newEmergencyContact) {
        return new ContactInfo(this.email, this.phoneNumber, this.address, newEmergencyContact);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ContactInfo that = (ContactInfo) obj;
        return Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(address, that.address) &&
                Objects.equals(emergencyContact, that.emergencyContact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, phoneNumber, address, emergencyContact);
    }

    @Override
    public String toString() {
        return String.format("ContactInfo{email='%s', phone='%s'}", email, phoneNumber);
    }
}