package com.mohitur.SpringDataJPA.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Entity
@Table(name = "student", schema = "public")
public class Student {

    @Id
    @Column(name = "roll_no")
    private Integer roll;

    @Column(name = "name")
    private String name;

    @Column(name = "marks")
    private int marks;

    @Lob
    @Column
    private byte[] profile;

    @Transient
    private String base64Profile;

    public Integer getRoll() {
        return roll;
    }

    public void setRoll(Integer roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public String getBase64Profile() {
        return base64Profile;
    }

    public void setBase64Profile(String base64Profile) {
        this.base64Profile = base64Profile;

        if (base64Profile != null && !base64Profile.isBlank()) {
            this.profile = Base64.getDecoder().decode(base64Profile);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNo=" + roll +
                ", name='" + name + '\'' +
                ", marks=" + marks +
                '}';
    }
}