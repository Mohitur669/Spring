package com.mohitur.SpringBootDemo.repo;

import com.mohitur.SpringBootDemo.model.Laptop;
import org.springframework.stereotype.Repository;

@Repository
public class LaptopRepository {
    public void save(Laptop lap) {
        System.out.println("Laptop saving in db");
    }
}