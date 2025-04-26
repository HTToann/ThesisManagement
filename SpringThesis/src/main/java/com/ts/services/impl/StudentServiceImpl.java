/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Major;
import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.repositories.MajorRepository;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.StudentService;
import java.util.Map;
import java.util.Set;
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
    private MajorRepository majorRepo;
    @Autowired
    private ThesisRepository thesisRepo;

    @Override
    public void insertStudent(Student student) {
        repo.insertStudent(student);
    }

    @Override
    public Student updateStudent(int id, Map<String, String> payload) {
        Student s = repo.getById(id);
        if (s == null) {
            throw new IllegalArgumentException("Không tìm thấy student");
        }

        // Cập nhật thesis nếu có
        String thesisIdStr = payload.get("thesisId");
        if (thesisIdStr != null && !thesisIdStr.trim().isEmpty()) {
            int thesisId = Integer.parseInt(thesisIdStr.trim());
            Thesis thesis = thesisRepo.getThesisById(thesisId);
            if (thesis == null) {
                throw new IllegalArgumentException("Không tìm thấy thesis với id = " + thesisId);
            }
            s.setThesisId(thesis);
        }

        // Cập nhật student_major nếu có majorId
        String majorIdStr = payload.get("majorId");
        if (majorIdStr != null && !majorIdStr.trim().isEmpty()) {
            int majorId = Integer.parseInt(majorIdStr.trim());
            Major majorEntity = majorRepo.getById(majorId);
            if (majorEntity == null) {
                throw new IllegalArgumentException("Không tìm thấy ngành với id = " + majorId);
            }

            s.setMajorSet(Set.of(majorEntity));  // cập nhật bảng student_major
        }

        return repo.updateStudent(s);
    }

    @Override
    public Student getStudentByUserId(int userID) {
        return repo.getStudentByUserId(userID);
    }

    @Override
    public void deleteStudent(int id) {
        Student s = this.repo.getById(id);
        if (s != null) {
            repo.deleleStudent(id);
        } else {
            throw new IllegalArgumentException("Không tồn tại id của student này");
        }
    }

}
