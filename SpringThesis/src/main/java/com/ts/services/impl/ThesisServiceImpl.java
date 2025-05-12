/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.services.ThesisService;
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
    public Thesis addThesis(Map<String, String> payload) {
        Thesis t = new Thesis();

        String title = payload.get("title");
        String description = payload.get("description");
        String semester = payload.get("semester");
        String yearStr = payload.get("year");
        String boardIdStr = payload.get("boardId");

        // Validate bắt buộc
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
            if (year < 1 || year > 3) {
                throw new IllegalArgumentException("Năm không hợp lệ (phải từ 1 đến 3)");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Năm phải là số");
        }

        // Validate hội đồng
        if (boardIdStr == null || boardIdStr.isBlank()) {
            throw new IllegalArgumentException("Hội đồng không được để trống");
        }

        int boardId;
        try {
            boardId = Integer.parseInt(boardIdStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Board ID phải là số");
        }

        Board board = boardRepo.getBoardById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("Không tìm thấy hội đồng");
        }

        // Kiểm tra số lượng đề tài trong hội đồng
        List<Thesis> existing = thesisRepo.getThesesByBoardId(boardId);
        if (existing.size() >= 5) {
            throw new IllegalArgumentException("Hội đồng này đã có tối đa 5 đề tài");
        }

        // Gán dữ liệu
        t.setTitle(title.trim());
        t.setSemester(semester.trim());
        t.setYear(year);
        t.setBoardId(board);
        t.setDescription(description != null ? description.trim() : null);

        Thesis saved = thesisRepo.addOrUpdate(t);

        // Gán sinh viên nếu có
        String studentIdStr = payload.get("studentId");
        if (studentIdStr != null && !studentIdStr.isBlank()) {
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

        String title = payload.get("title");
        String description = payload.get("description");
        String semester = payload.get("semester");
        String yearStr = payload.get("year");
        String boardIdStr = payload.get("boardId");

        // Validate bắt buộc
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
            if (year <= 0) {
                throw new IllegalArgumentException("Năm không hợp lệ");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Năm phải là số");
        }

        if (boardIdStr == null || boardIdStr.isBlank()) {
            throw new IllegalArgumentException("Hội đồng không được để trống");
        }

        int boardId;
        try {
            boardId = Integer.parseInt(boardIdStr.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Board ID phải là số");
        }

        Board board = boardRepo.getBoardById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("Không tìm thấy hội đồng");
        }

        // Cập nhật dữ liệu
        t.setTitle(title.trim());
        t.setSemester(semester.trim());
        t.setYear(year);
        t.setBoardId(board);
        t.setDescription(description != null ? description.trim() : null);

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
