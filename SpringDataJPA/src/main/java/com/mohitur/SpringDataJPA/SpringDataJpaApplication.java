package com.mohitur.SpringDataJPA;

import com.mohitur.SpringDataJPA.model.Student;
import com.mohitur.SpringDataJPA.repo.StudentRepo;
import com.mohitur.SpringDataJPA.service.StudentEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringDataJpaApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringDataJpaApplication.class, args);
        StudentEndPoint studentEndPoint = context.getBean(StudentEndPoint.class);

        studentEndPoint.addCreateStudent();
    }

}