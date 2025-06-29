package com.mohitur.SpringJDBCEx.service;

import com.mohitur.SpringJDBCEx.model.Student;
import com.mohitur.SpringJDBCEx.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {


    private StudentRepo repo;

    public StudentRepo getRepo() {
        return repo;
    }

    @Autowired
    public void setRepo(StudentRepo repo) {
        this.repo = repo;
    }

    public void addStudent(Student s) {
        repo.save(s);
    }

    public void deleteStudent(int rollNo) {
        repo.deleteStudent(rollNo);
    }

    public List<Student> getStudents() {
        return repo.findAll();
    }

    public Student getStudentById(int id) {
        return repo.getStudentById(id);
    }
}