package com.mohitur.SpringDataJPA.repo;

import com.mohitur.SpringDataJPA.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {

    @Query("select pp from Student pp where pp.rollNo = ?1 and pp.name = ?2")
    public Student findStudentByIdAndName(int rollNo, String name);

    public Student findById(int roll);
}