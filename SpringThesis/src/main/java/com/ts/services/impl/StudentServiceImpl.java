/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Major;
import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.Users;
import com.ts.repositories.MajorRepository;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.StudentService;
import java.util.List;
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
    private UsersRepository userRepo;
    @Autowired
    private ThesisRepository thesisRepo;

    @Override
    public void insertStudent(Map<String, String> payload) {
//        repo.insertStudent(student);
        String userIdStr = payload.get("userId");
        String thesisIdStr = payload.get("thesisId");
        String majorIdStr = payload.get("majorId");

        if (userIdStr == null || majorIdStr == null) {
            throw new IllegalArgumentException("Thiếu dữ liệu bắt buộc");
        }

        int userId = Integer.parseInt(userIdStr.trim());
        Users user = userRepo.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại");
        }

        Student s = new Student();
        s.setUserId(user);
        if (thesisIdStr != null && !thesisIdStr.trim().isEmpty()) {
            int thesisId = Integer.parseInt(thesisIdStr.trim());
            Thesis thesis = thesisRepo.getThesisById(thesisId);
            if (thesis == null) {
                throw new IllegalArgumentException("Khóa luận không tồn tại");
            }
            s.setThesisId(thesis);
        }

        int majorId = Integer.parseInt(majorIdStr.trim());
        Major major = majorRepo.getById(majorId);
        if (major == null) {
            throw new IllegalArgumentException("Ngành không tồn tại");
        }
        s.setMajorSet(Set.of(major));
        repo.insertStudent(s);
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

    @Override
    public List<Student> getAll() {
        return this.repo.getAll();
    }

    @Override
    public List<Student> getStudentsByUsername(String kw) {
        return this.repo.getStudentByUsername(kw);
    }

   

}
