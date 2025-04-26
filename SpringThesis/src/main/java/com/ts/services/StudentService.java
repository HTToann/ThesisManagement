/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Student;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface StudentService {
    void insertStudent(Student student);
    Student getStudentByUserId(int userID);
    Student updateStudent(int id, Map<String, String> payload);
    void deleteStudent(int id);
}
