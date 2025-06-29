package com.mohitur.SpringJDBCEx;

import com.mohitur.SpringJDBCEx.model.Student;
import com.mohitur.SpringJDBCEx.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class SpringJdbcExApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringJdbcExApplication.class, args);

        Student s = context.getBean(Student.class);

//        s.setRollNo(6);
//        s.setName("Nagma");
//        s.setMarks(100);

        StudentService service = context.getBean(StudentService.class);
//        service.addStudent(s);

        List<Student> studentList = service.getStudents();
        System.out.println(studentList);

        service.deleteStudent(3);

        List<Student> lstStudent = service.getStudents();
        System.out.println(lstStudent);

        System.out.println(service.getStudentById(6).toString());
    }

}