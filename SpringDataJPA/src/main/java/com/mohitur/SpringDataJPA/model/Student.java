package com.mohitur.SpringDataJPA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;

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


    @Override
    public String toString() {
        return "Student{" +
                "rollNo=" + roll +
                ", name='" + name + '\'' +
                ", marks=" + marks +
                '}';
    }
}