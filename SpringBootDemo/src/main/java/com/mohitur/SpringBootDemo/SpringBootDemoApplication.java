package com.mohitur.SpringBootDemo;

import com.mohitur.SpringBootDemo.model.Alien;
import com.mohitur.SpringBootDemo.model.Laptop;
import com.mohitur.SpringBootDemo.service.LaptopService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringBootDemoApplication.class, args);


        LaptopService service = context.getBean(LaptopService.class);

        Laptop lap = context.getBean(Laptop.class);
        service.addLaptop(lap);

//		Alien alien = context.getBean(Alien.class);
//		alien.code();

    }

}