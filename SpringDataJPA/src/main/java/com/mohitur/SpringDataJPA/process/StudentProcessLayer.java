package com.mohitur.SpringDataJPA.process;


import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentProcessLayer {

    @Autowired
    private StudentRepo studentRepo;

    public void addCreateStudent() {
        Student student = new Student();
        student.setRoll(101);
        student.setName("Yoyo");
        student.setMarks(78);

        studentRepo.save(student);
        System.out.println(studentRepo.findAll());
    }

    public Student addCreateStudentAPI(Student student) {
        System.out.println(studentRepo.findAll());
        return studentRepo.save(student);
    }

    public List<Student> findStudents(Integer roll, String name) {
        if (roll != null && name != null && !name.isBlank()) {
            return studentRepo.findByRollAndNameIgnoreCase(roll, name);
        } else if (roll != null) {
            Student student = studentRepo.findByRoll(roll);
            return student != null ? List.of(student) : List.of();
        } else if (name != null && !name.isBlank()) {
            return studentRepo.findByNameIgnoreCase(name);
        } else {
            return List.of();
        }
    }

    public Student findByRoll(Integer roll) {
        return studentRepo.findByRoll(roll);
    }

    public List<Student> findStudentsByName(String name) {
        return studentRepo.findByNameIgnoreCase(name);
    }
}