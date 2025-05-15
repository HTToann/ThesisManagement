/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Student;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface StudentService {
    void insertStudent(Map<String, String> payload);
    Student getStudentByUserId(int userID);
    List<Student> getStudentsByThesisId(int thesisId);
    List<Student> getStudentsByUsername(String kw);
    List<Student> getAll();
    Student updateStudent(int id, Map<String, String> payload);
    void deleteStudent(int id);
}
