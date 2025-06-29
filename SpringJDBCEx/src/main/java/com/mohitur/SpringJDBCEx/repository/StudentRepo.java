package com.mohitur.SpringJDBCEx.repository;

import com.mohitur.SpringJDBCEx.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepo {

    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public void save(Student s) {
        String sql = "insert into student (rollNo, name, marks) values(?, ?, ?)";

        int rows = jdbc.update(sql, s.getRollNo(), s.getName(), s.getMarks());
        System.out.println(rows + " effected");
    }

    public List<Student> findAll() {

        String sql = "select * from student";

        return jdbc.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setRollNo(rs.getInt("rollNo"));
            s.setName(rs.getString("name"));
            s.setMarks(rs.getInt("marks"));

            return s;
        });
    }

    public void deleteStudent(int rollNo) {

        String sql = "delete from mohitur.public.student where rollno = ?";


        int update = jdbc.update(sql, rollNo);
        System.out.println(update + " rows affected");
    }

    public Student getStudentById(int id) {
        List<Student> lstStudent = findAll();

        Student s = new Student();
        for (Student student : lstStudent) {
            if (student.getRollNo() == id) {
                s.setRollNo(student.getRollNo());
                s.setName(student.getName());
                s.setMarks(student.getMarks());
                break;
            }
        }

        return s;
    }
}