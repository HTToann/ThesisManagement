/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisLecturer;
import com.ts.pojo.ThesisLecturerPK;
import com.ts.pojo.Users;
import com.ts.repositories.ThesisLecturerRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.ThesisLecturerService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class ThesisLecturerServiceImpl implements ThesisLecturerService {

    @Autowired
    private ThesisLecturerRepository repo;
    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private UsersRepository userRepo;

    @Override
    public void add(Map<String, String> payload) {
        try {
            int thesisId = Integer.parseInt(payload.get("thesis_id").trim());
            int lecturerId = Integer.parseInt(payload.get("lecturer_id").trim());
            String role = payload.get("role").trim();

            if (role == null || (!role.equals("MAIN_ADVISOR") && !role.equals("CO_ADVISOR"))) {
                throw new IllegalArgumentException("Vai trò không hợp lệ. Chỉ chấp nhận 'MAIN_ADVISOR' hoặc 'CO_ADVISOR'.");
            }

            // Lấy đối tượng liên quan
            Thesis thesis = thesisRepo.getThesisById(thesisId);
            Users lecturer = userRepo.getUserById(lecturerId);

            if (thesis == null || lecturer == null) {
                throw new IllegalArgumentException("Thesis hoặc giảng viên không tồn tại.");
            }

            // Lấy danh sách giảng viên đã phân công cho đề tài
            List<ThesisLecturer> lecturers = repo.getByThesisId(thesisId);

            if (lecturers.size() >= 2) {
                throw new IllegalArgumentException("Mỗi đề tài chỉ được tối đa 2 giảng viên hướng dẫn.");
            }

            for (ThesisLecturer l : lecturers) {
                if (l.getUsers().getUserId().equals(lecturerId)) {
                    throw new IllegalArgumentException("Giảng viên này đã được phân công cho đề tài.");
                }
                if (l.getLectureRole().equalsIgnoreCase(role)) {
                    throw new IllegalArgumentException("Vai trò '" + role + "' đã được sử dụng cho đề tài này.");
                }
            }

            // Tạo mới đối tượng và persist
            ThesisLecturer tl = new ThesisLecturer();
            tl.setThesis(thesis);
            tl.setUsers(lecturer);
            tl.setLectureRole(role);
// Bạn phải set cái PK
            tl.setThesisLecturerPK(new ThesisLecturerPK(thesis.getThesisId(), lecturer.getUserId()));
            repo.add(tl);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Thesis ID hoặc Lecturer ID không hợp lệ.");
        }
    }

    @Override
    public List<ThesisLecturer> getByThesisId(int thesisId) {
        return repo.getByThesisId(thesisId);
    }

    @Override
    public void delete(Map<String, String> payload) {
        try {
            int thesisId = Integer.parseInt(payload.get("thesis_id").trim());
            int lecturerId = Integer.parseInt(payload.get("lecturer_id").trim());

            ThesisLecturer tl = repo.getByCompositeKey(thesisId, lecturerId);
            if (tl == null) {
                throw new IllegalArgumentException("Không tồn tại phân công với thesis_id và lecturer_id này.");
            }

            repo.delete(thesisId, lecturerId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Thesis ID hoặc Lecturer ID không hợp lệ.");
        }
    }

    @Override
    public void updateLecturer(Map<String, String> payload) {
        try {
            int thesisId = Integer.parseInt(payload.get("thesis_id").trim());
            int oldLecturerId = Integer.parseInt(payload.get("old_lecturer_id").trim());
            int newLecturerId = Integer.parseInt(payload.get("new_lecturer_id").trim());

            // Ràng buộc
            if (oldLecturerId == newLecturerId) {
                throw new IllegalArgumentException("Lecturer cũ và mới không được trùng nhau.");
            }

            ThesisLecturer existing = repo.getByCompositeKey(thesisId, oldLecturerId);
            if (existing == null) {
                throw new IllegalArgumentException("Không tồn tại phân công giảng viên cần thay thế.");
            }

            Users newLecturer = userRepo.getUserById(newLecturerId);
            if (newLecturer == null) {
                throw new IllegalArgumentException("Giảng viên mới không tồn tại.");
            }

            List<ThesisLecturer> currentLecturers = repo.getByThesisId(thesisId);
            for (ThesisLecturer l : currentLecturers) {
                if (l.getUsers().getUserId().equals(newLecturerId)) {
                    throw new IllegalArgumentException("Giảng viên mới đã được phân công.");
                }
            }

            // Xoá giảng viên cũ
            repo.delete(thesisId, oldLecturerId);

            // Tạo mới với giảng viên mới và role cũ
            ThesisLecturer newTL = new ThesisLecturer();
            newTL.setThesis(existing.getThesis());
            newTL.setUsers(newLecturer);
            newTL.setLectureRole(existing.getLectureRole());
            newTL.setThesisLecturerPK(new ThesisLecturerPK(thesisId, newLecturerId));

            repo.add(newTL);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID không hợp lệ.");
        }
    }

}
