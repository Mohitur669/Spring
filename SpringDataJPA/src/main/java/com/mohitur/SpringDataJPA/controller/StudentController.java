package com.mohitur.SpringDataJPA.controller;

import com.mohitur.SpringDataJPA.DTO.ApiResponse;
import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.process.StudentProcessLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired
    private StudentProcessLayer studentProcessLayer;

    @PostMapping("/addStudent")
    public ResponseEntity<String> addStudent() {
        studentProcessLayer.addCreateStudent();
        return ResponseEntity.ok("Sample student added.");
    }

    @PostMapping("/createStudentFromAPI")
    public ResponseEntity<ApiResponse> createStudentFromAPI(@RequestBody Student student) {
        Student savedStudent = studentProcessLayer.addCreateStudentAPI(student);
        ApiResponse response = new ApiResponse("Student created successfully", savedStudent);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/findStudents")
    public ResponseEntity<?> findStudents(
            @RequestParam(value = "roll", required = false) Integer roll,
            @RequestParam(value = "name", required = false) String name) {

        List<Student> students = studentProcessLayer.findStudents(roll, name);

        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching students found.");
        }

        return ResponseEntity.ok(students);
    }

    @GetMapping("/findStudentById")
    public ResponseEntity<?> findStudentById(@RequestParam("roll") Integer roll) {
        Student student = studentProcessLayer.findByRoll(roll);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with roll: " + roll);
        }

        return ResponseEntity.ok(student);
    }
}