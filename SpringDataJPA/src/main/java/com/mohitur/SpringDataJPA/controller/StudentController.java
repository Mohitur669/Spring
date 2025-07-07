package com.mohitur.SpringDataJPA.controller;

import com.mohitur.SpringDataJPA.DTO.ApiResponse;
import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.process.StudentProcessLayer;
import com.mohitur.SpringDataJPA.repo.StudentRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    private static final Logger log = LogManager.getLogger(StudentController.class);
    @Autowired
    private StudentProcessLayer studentProcessLayer;
    @Autowired
    private StudentRepo studentRepo;

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

    @PostMapping("/updateStudent")
    public ResponseEntity<ApiResponse> updateStudent(@RequestBody Student student) {
        Student updated = studentProcessLayer.updateStudent(student);
        if (updated != null) {
            return ResponseEntity.ok(new ApiResponse("Student updated successfully", updated));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Student not found", null));
        }
    }

    @PostMapping("/uploadProfile")
    public ResponseEntity<?> uploadProfile(
            @RequestParam("roll") int roll,
            @RequestParam("image") MultipartFile file) {

        try {
            Student student = studentProcessLayer.findByRoll(roll);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
            }
            studentProcessLayer.uploadProfile(student, file);
            return ResponseEntity.ok("Profile image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    @Transactional
    @GetMapping("/profile")
    public ResponseEntity<byte[]> getProfileImage(@RequestParam("roll") int roll) {
        Student student = studentProcessLayer.findByRoll(roll);
        if (student == null || student.getProfile() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Optional: you can check file signature to determine content-type
        byte[] imageBytes = student.getProfile();
        MediaType mediaType = studentProcessLayer.detectImageType(imageBytes); // see helper below

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }

    @PostMapping("/deleteStudent")
    public ResponseEntity<ApiResponse> deleteStudent(@RequestParam("roll") Integer roll) {
       try {
           Student deletedStudent = studentProcessLayer.deleteStudent(roll);
           ApiResponse response = new ApiResponse("Student deleted successfully", deletedStudent);
           return ResponseEntity.status(HttpStatus.OK).body(response);
       } catch (IOException ex) {
           log.debug("Student does not exists!");
       }

       return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}