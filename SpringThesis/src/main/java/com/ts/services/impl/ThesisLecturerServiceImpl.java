/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.enumRole.ThesisLecturerRole;
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
import java.util.Set;
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
            int thesisId = Integer.parseInt(payload.get("thesisId").trim());
            int lecturerId = Integer.parseInt(payload.get("lecturerId").trim());
            String role = payload.get("lectureRole");

            Set<String> thesisLecturerRoles = Set.of(
                    ThesisLecturerRole.ROLE_MAIN_ADVISOR.name(),
                    ThesisLecturerRole.ROLE_CO_ADVISOR.name()
            );
            if (!thesisLecturerRoles.contains(role)) {
                throw new IllegalArgumentException("Vai trò không hợp lệ. Chỉ chấp nhận 'ROLE_MAIN_ADVISOR' hoặc 'ROLE_CO_ADVISOR'.");
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
            tl.setLectureRole(role.trim());
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
            int thesisId = Integer.parseInt(payload.get("thesisId").trim());
            int lecturerId = Integer.parseInt(payload.get("lecturerId").trim());

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
            int thesisId = Integer.parseInt(payload.get("thesisId").trim());
            int oldLecturerId = Integer.parseInt(payload.get("oldLecturerId").trim());
            int newLecturerId = Integer.parseInt(payload.get("newLecturerId").trim());
            String newRole = payload.get("lectureRole");

            Set<String> thesisLecturerRoles = Set.of(
                    ThesisLecturerRole.ROLE_MAIN_ADVISOR.name(),
                    ThesisLecturerRole.ROLE_CO_ADVISOR.name()
            );
            if (newRole == null || newRole.trim().isEmpty()) {
                throw new IllegalArgumentException("Vai trò không được để trống.");
            }
            if (!thesisLecturerRoles.contains(newRole)) {
                throw new IllegalArgumentException("Vai trò không hợp lệ. Chỉ chấp nhận 'ROLE_MAIN_ADVISOR' hoặc 'ROLE_CO_ADVISOR'.");
            }

            // Tìm bản ghi hiện tại
            ThesisLecturer current = repo.getByCompositeKey(thesisId, oldLecturerId);
            if (current == null) {
                throw new IllegalArgumentException("Không tìm thấy bản ghi để cập nhật.");
            }

            // Nếu chỉ đổi vai trò (giữ nguyên lecturer)
            if (oldLecturerId == newLecturerId) {
                current.setLectureRole(newRole.trim());
                repo.updateRole(current);
                return;
            }

            // Nếu đổi lecturer → kiểm tra giảng viên mới
            Users newLecturer = userRepo.getUserById(newLecturerId);
            if (newLecturer == null) {
                throw new IllegalArgumentException("Giảng viên mới không tồn tại.");
            }

            List<ThesisLecturer> existingLecturers = repo.getByThesisId(thesisId);
            for (ThesisLecturer tl : existingLecturers) {
                if (tl.getUsers().getUserId().equals(newLecturerId)) {
                    throw new IllegalArgumentException("Giảng viên mới đã được phân công.");
                }
            }

            // Xoá cũ → thêm mới
            repo.delete(thesisId, oldLecturerId);

            ThesisLecturer updated = new ThesisLecturer();
            updated.setThesis(current.getThesis());
            updated.setUsers(newLecturer);
            updated.setLectureRole(newRole.trim());
            updated.setThesisLecturerPK(new ThesisLecturerPK(thesisId, newLecturerId));

            repo.add(updated);

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID không hợp lệ.");
        }
    }

    @Override
    public List<ThesisLecturer> getAll() {
        return this.repo.getAll();
    }

    @Override
    public List<ThesisLecturer> getByThesisName(String kw) {
        return repo.getByThesisName(kw);
    }

}
