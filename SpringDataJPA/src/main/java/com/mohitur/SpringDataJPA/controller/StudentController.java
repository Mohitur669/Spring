package com.mohitur.SpringDataJPA.controller;

import com.mohitur.SpringDataJPA.DTO.ApiResponse;
import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.process.StudentProcessLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired
    private StudentProcessLayer studentProcessLayer;

    @PostMapping("/addStudent")
    public void addStudent() {
        studentProcessLayer.addCreateStudent();
    }

    @PostMapping("/createStudentFromAPI")
    public ResponseEntity<ApiResponse> createStudentFromAPI(@RequestBody Student student) {
        Student savedStudent = studentProcessLayer.addCreateStudentAPI(student);
        ApiResponse response = new ApiResponse("Student created successfully", savedStudent);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/findStudent")
    public Student findStudent(@RequestParam("roll") int roll, @RequestParam("name") String name) {
        return studentProcessLayer.findStudent(roll, name);
    }

    @GetMapping("/findStudentById")
    public Student findStudentById(@RequestParam("roll") int roll) {
        return studentProcessLayer.findStudentById(roll);
    }
}