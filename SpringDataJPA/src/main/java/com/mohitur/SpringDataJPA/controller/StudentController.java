package com.mohitur.SpringDataJPA.controller;

import com.mohitur.SpringDataJPA.DTO.ApiResponse;
import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.process.StudentProcessLayer;
import com.mohitur.SpringDataJPA.repo.StudentRepo;
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

    @PutMapping("/updateStudent")
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
            Student student = studentRepo.findByRoll(roll);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
            }
            student.setProfile(file.getBytes());
            studentRepo.save(student);
            return ResponseEntity.ok("Profile image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    @Transactional
    @GetMapping("/profile")
    public ResponseEntity<byte[]> getProfileImage(@RequestParam("roll") int roll) {
        Student student = studentRepo.findByRoll(roll);
        if (student == null || student.getProfile() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Optional: you can check file signature to determine content-type
        byte[] imageBytes = student.getProfile();
        MediaType mediaType = detectImageType(imageBytes); // see helper below

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }

    // Helper method to detect image type by magic number
    private MediaType detectImageType(byte[] imageBytes) {
        if (imageBytes.length >= 8 &&
                imageBytes[0] == (byte) 0x89 &&
                imageBytes[1] == (byte) 0x50 &&
                imageBytes[2] == (byte) 0x4E &&
                imageBytes[3] == (byte) 0x47) {
            return MediaType.IMAGE_PNG;
        } else if (imageBytes.length >= 3 &&
                imageBytes[0] == (byte) 0xFF &&
                imageBytes[1] == (byte) 0xD8 &&
                imageBytes[2] == (byte) 0xFF) {
            return MediaType.IMAGE_JPEG;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}