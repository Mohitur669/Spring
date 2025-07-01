package com.mohitur.SpringDataJPA;

import com.mohitur.SpringDataJPA.controller.StudentController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringDataJpaApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringDataJpaApplication.class, args);
        StudentController studentEndPoint = context.getBean(StudentController.class);

        studentEndPoint.addStudent();
    }

}