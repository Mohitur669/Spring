package com.mohitur.SpringDataJPA.process;


import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentProcessLayer {

    @Autowired
    private StudentRepo studentRepo;

    public void addCreateStudent() {
        Student student = new Student();
        student.setRollNo(101);
        student.setName("Yoyo");
        student.setMarks(78);

        studentRepo.save(student);
        System.out.println(studentRepo.findAll());
    }

    public Student addCreateStudentAPI(Student student) {
        System.out.println(studentRepo.findAll());
        return studentRepo.save(student);
    }

    public Student findStudent(int roll, String name) {
        return studentRepo.findStudentByIdAndName(roll, name);
    }

    public Student findStudentById(int roll) {
        return studentRepo.findById(roll);
    }
}