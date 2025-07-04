package com.mohitur.SpringDataJPA.repo;

import com.mohitur.SpringDataJPA.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {

    public Student findByRoll(Integer roll);

    public List<Student> findByNameIgnoreCase(String name);

    public List<Student> findByRollAndNameIgnoreCase(Integer rollNo, String name);



}