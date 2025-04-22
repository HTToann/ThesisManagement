/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Student;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface StudentRepository {
    void insertStudent(Student s);
    public Student updateStudent(Student s);
    Student getStudentByUserId(int userId);
    void deleleStudent(int userId);
}
