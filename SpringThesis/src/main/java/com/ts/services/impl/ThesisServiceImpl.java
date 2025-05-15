/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.Users;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.ThesisService;
import java.security.Principal;
import java.time.Year;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class ThesisServiceImpl implements ThesisService {

    @Autowired
    private ThesisRepository thesisRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private UsersRepository userRepo;

    @Override
    public Thesis getThesisById(int thesisId) {
        return this.thesisRepo.getThesisById(thesisId);
    }

    @Override
    public void deleteThesisById(int thesisId) {
        if (this.thesisRepo.getThesisById(thesisId) != null) {
            this.deleteThesisById(thesisId);
        } else {
            throw new IllegalArgumentException("Không tồn tại thesisId");
        }
    }

    @Override
    public List<Thesis> getAllThesis() {
        return this.thesisRepo.getAllThesis();
    }

    @Override
    public Thesis addThesis(Map<String, String> payload, Principal principal) {
        String username = principal.getName();
        Users currentUser = userRepo.getUserByUsername(username);

        Thesis t = new Thesis();

        String title = payload.get("title");
        String description = payload.get("description");
        String semester = payload.get("semester");
        String yearStr = payload.get("year");
        String boardIdStr = payload.get("boardId"); // bỏ qua nếu là student
        String studentIdStr = payload.get("studentId"); // bỏ qua nếu là student

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Tiêu đề không được để trống");
        }

        if (semester == null || semester.isBlank()) {
            throw new IllegalArgumentException("Học kỳ không được để trống");
        }

        if (yearStr == null || yearStr.isBlank()) {
            throw new IllegalArgumentException("Năm không được để trống");
        }
        int year;
        try {
            year = Integer.parseInt(yearStr.trim());
            int currentYear = Year.now().getValue(); 
            if (year <= 0 || year > currentYear) {
                throw new IllegalArgumentException("Năm không hợp lệ ");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Năm phải là số");
        }
        int semesterInt;
        try {
            semesterInt = Integer.parseInt(semester.trim());
            if (semesterInt < 1 || semesterInt > 3) {
                throw new IllegalArgumentException("Học kỳ không hợp lệ (1 - 3)");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Học kỳ phải là số");
        }

        Board board = null;

        // Nếu KHÔNG phải là STUDENT, cho phép gán hội đồng
        if (!"ROLE_STUDENT".equalsIgnoreCase(currentUser.getRole())) {
            if (boardIdStr != null && !boardIdStr.isBlank()) {
                try {
                    int boardId = Integer.parseInt(boardIdStr.trim());
                    board = boardRepo.getBoardById(boardId);
                    if (board == null) {
                        throw new IllegalArgumentException("Không tìm thấy hội đồng");
                    }
                    if (thesisRepo.getThesesByBoardId(boardId).size() >= 5) {
                        throw new IllegalArgumentException("Hội đồng này đã có tối đa 5 đề tài");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Board ID phải là số");
                }
            }
        }
        // Gán các trường
        t.setTitle(title.trim());
        t.setSemester(semester.trim());
        t.setYear(year);
        t.setBoardId(board);
        t.setDescription(description != null ? description.trim() : null);

        Thesis saved = thesisRepo.addOrUpdate(t);

        // Gán sinh viên nếu là ROLE_STUDENT
        if ("ROLE_STUDENT".equalsIgnoreCase(currentUser.getRole())) {
            Student s = studentRepo.getStudentByUserId(currentUser.getUserId());
            if (s == null) {
                throw new IllegalArgumentException("Không tìm thấy thông tin sinh viên cho user hiện tại");
            }
            s.setThesisId(saved);
            studentRepo.updateStudent(s);
        } // Nếu là admin tạo, có thể gán sinh viên khác
        else if (studentIdStr != null && !studentIdStr.isBlank()) {
            try {
                int studentId = Integer.parseInt(studentIdStr.trim());
                Student s = studentRepo.getStudentByUserId(studentId);
                if (s == null) {
                    throw new IllegalArgumentException("Không tìm thấy sinh viên với ID: " + studentId);
                }
                s.setThesisId(saved);
                studentRepo.updateStudent(s);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Student ID không hợp lệ");
            }
        }

        return saved;
    }

    @Override
    public Thesis updateThesis(int id, Map<String, String> payload) {
        Thesis t = thesisRepo.getThesisById(id);
        if (t == null) {
            throw new IllegalArgumentException("Không tìm thấy đề tài.");
        }

        if (payload.containsKey("title")) {
            String title = payload.get("title");
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Tiêu đề không được để trống");
            }
            t.setTitle(title.trim());
        }

        if (payload.containsKey("description")) {
            String description = payload.get("description");
            t.setDescription(description != null ? description.trim() : null);
        }

        if (payload.containsKey("semester")) {
            String semester = payload.get("semester");
            if (semester == null || semester.isBlank()) {
                throw new IllegalArgumentException("Học kỳ không được để trống");
            }
            int semesterInt;
            try {
                semesterInt = Integer.parseInt(semester.trim());
                if (semesterInt < 1 || semesterInt > 3) {
                    throw new IllegalArgumentException("Học kỳ không hợp lệ (1 - 3)");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Học kỳ phải là số");
            }
            t.setSemester(semester.trim());
        }

        if (payload.containsKey("year")) {
            String yearStr = payload.get("year");
            if (yearStr == null || yearStr.isBlank()) {
                throw new IllegalArgumentException("Năm không được để trống");
            }
            try {
                int year = Integer.parseInt(yearStr.trim());
                int currentYear = Year.now().getValue();
                if (year <= 0 || year > currentYear) {
                    throw new IllegalArgumentException("Năm không hợp lệ");
                }
                t.setYear(year);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Năm phải là số");
            }
        }

        if (payload.containsKey("boardId")) {
            String boardIdStr = payload.get("boardId");
            if (boardIdStr == null || boardIdStr.isBlank()) {
                throw new IllegalArgumentException("Hội đồng không được để trống");
            }
            try {
                int boardId = Integer.parseInt(boardIdStr.trim());
                Board board = boardRepo.getBoardById(boardId);
                if (board == null) {
                    throw new IllegalArgumentException("Không tìm thấy hội đồng");
                }

                // Chỉ kiểm tra nếu gán sang hội đồng MỚI hoặc đề tài chưa có hội đồng
                if (t.getBoardId() == null || t.getBoardId().getBoardId() != boardId) {
                    List<Thesis> existing = thesisRepo.getThesesByBoardId(boardId);
                    if (existing.size() >= 5) {
                        throw new IllegalArgumentException("Hội đồng này đã có tối đa 5 đề tài");
                    }
                }

                t.setBoardId(board);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Board ID phải là số");
            }
        }

        return thesisRepo.addOrUpdate(t);
    }

    @Override
    public List<Thesis> getThesisByName(String kw) {
        return this.thesisRepo.getThesisByName(kw);
    }

    @Override
    public Thesis updateBrowsingThesis(int id, Map<String, String> payload) {
        Thesis t = this.thesisRepo.getThesisById(id);
        if (t == null) {
            throw new IllegalArgumentException("Không tìm thấy đề tài.");
        }
        String isLocked = payload.get("isLocked");

        if (isLocked != null && isLocked.trim().equalsIgnoreCase("true")) {
            t.setIsLocked(Boolean.TRUE);
        } else if (isLocked != null && isLocked.trim().equalsIgnoreCase("false")) {
            t.setIsLocked(Boolean.FALSE);
        } else {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }
        return this.thesisRepo.addOrUpdate(t);
    }
}
