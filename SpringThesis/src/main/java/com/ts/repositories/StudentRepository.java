/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Student;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface StudentRepository {
    void insertStudent(Student s);
    List<Student> getAll();
    Student getById(int id);
    public Student updateStudent(Student s);
    Student getByThesisId(int thesisId);
    Student getStudentByUserId(int userId);
    List<Student> getStudentByUsername(String kw);
    void deleleStudent(int userId);
}
