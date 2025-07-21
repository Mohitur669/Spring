package com.mohitur.SpringDataJPA.process;


import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.repo.StudentRepo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StudentProcessLayer {

    private static final Logger log = LoggerFactory.getLogger(StudentProcessLayer.class);
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

    public Student updateStudent(Student student) {
        Student existing = studentRepo.findByRoll(student.getRoll());
        if (existing != null) {
            existing.setName(student.getName());
            existing.setMarks(student.getMarks());
            return studentRepo.save(existing);
        }
        return null;
    }

    // Helper method to detect image type by magic number
    public MediaType detectImageType(byte[] imageBytes) {
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

    public Student deleteStudent(Integer roll) throws IOException {
        Student deletedStudent = studentRepo.findByRoll(roll);
        studentRepo.delete(deletedStudent);
        return deletedStudent;
    }

    public void uploadProfile(Student student, MultipartFile file) throws IOException {
        student.setProfile(file.getBytes());
        studentRepo.save(student);
    }

    public List<Student> deleteStudentsBulk(List<Integer> rollNumbers) {
        List<Student> deletedStudents = new ArrayList<>();
        for (Integer roll : rollNumbers) {
            Student student = findByRoll(roll);
            if (student != null) {
                studentRepo.delete(student);
                deletedStudents.add(student);
            }
        }
        return deletedStudents;
    }

    public ResponseEntity<String> uploadExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header
                int roll = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                int marks = (int) row.getCell(2).getNumericCellValue();

                Optional<Student> existing = studentRepo.findById(roll);
                if (existing.isPresent()) {
                    Student s = existing.get();
                    s.setName(name);
                    s.setMarks(marks);
                    studentRepo.save(s);
                } else {
                    Student s = new Student();
                    s.setRoll(roll);
                    s.setName(name);
                    s.setMarks(marks);
                    studentRepo.save(s);
                }
            }

            return ResponseEntity.ok("Excel processed successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Excel format");
        }
    }

}