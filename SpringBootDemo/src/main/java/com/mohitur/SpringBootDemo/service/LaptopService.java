package com.mohitur.SpringBootDemo.service;

import com.mohitur.SpringBootDemo.repo.LaptopRepository;
import com.mohitur.SpringBootDemo.model.Laptop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaptopService {

    @Autowired
    private LaptopRepository repo;

    public void addLaptop(Laptop lap) {
        repo.save(lap);
    }

    public boolean isGoodForCode(Laptop lap) {
        return true;
    }
}