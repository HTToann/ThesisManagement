/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.StudentService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repo;
    @Autowired
    private ThesisRepository thesisRepo;
    @Override
    public void insertStudent(Student student) {
        repo.insertStudent(student);
    }

    @Override
    public Student updateStudent(int userId, Map<String, String> payload) {
        Student s = repo.getStudentByUserId(userId);
        if (s == null) {
            throw new IllegalArgumentException("Không tìm thấy student có userId = " + userId);
        }
        String major = payload.get("major").trim();
        String thesisIdStr = payload.get("thesisId").trim();
        if (major != null && !major.trim().isEmpty()) {
            s.setMajor(major);
        }
        if (thesisIdStr != null && !thesisIdStr.trim().isEmpty()) {
            int thesisId = Integer.parseInt(thesisIdStr);
            Thesis thesis = thesisRepo.getThesisById(thesisId);
            if (thesis == null) {
                throw new IllegalArgumentException("Không tìm thấy thesis với id = " + thesisId);
            }
            s.setThesisId(thesis);
        }
        return repo.updateStudent(s);
    }

    @Override
    public Student getStudentByUserId(int userID) {
        return repo.getStudentByUserId(userID);
    }

    @Override
    public void deleteStudent(int userId) {
        Student s = this.getStudentByUserId(userId);
        if ( s != null)
            repo.deleleStudent(userId);
        else 
            throw new IllegalArgumentException("Không tồn tại userId của student này");
    }

}
