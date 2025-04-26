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

        String board_id = payload.get("boardId");
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title không được để trống");
        }
        if (yearStr == null || yearStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Year không được để trống");
        }

        int year = Integer.parseInt(yearStr.trim());
        if (year < 1 || year >3) {
            throw new IllegalArgumentException("Year không hợp lệ");
        }
        if (description != null) {
            t.setDescription(description);
        }
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester không được để trống");
        }

        if (board_id == null || board_id.trim().isEmpty()) {
            throw new IllegalArgumentException("BoardId không được để trống");
        }

        if (board_id != null) {
            int id = Integer.parseInt(board_id.trim());
            Board b = boardRepo.getBoardById(id);
            if (b == null) {
                throw new IllegalArgumentException("Board không tồn tại");
            }

            // 💡 Kiểm tra số lượng thesis đã thuộc về hội đồng này
            List<Thesis> thesisList = thesisRepo.getThesesByBoardId(id);  // 👈 bạn cần thêm hàm này
            if (thesisList.size() >= 5) {
                throw new IllegalArgumentException("Hội đồng này đã được phân công tối đa 5 đề tài.");
            }
            t.setBoardId(b);
        }
        t.setTitle(title.trim());
        t.setYear(year);
        Thesis savedThesis = this.thesisRepo.addOrUpdate(t);
// 🔹 Gắn sinh viên nếu có
        String studentIdStr = payload.get("studentId");  // key đổi từ studentIds → studentId (dạng chuỗi)
        if (studentIdStr != null && !studentIdStr.trim().isEmpty()) {
            int studentId = Integer.parseInt(studentIdStr.trim());
            Student s = studentRepo.getStudentByUserId(studentId);
            if (s == null) {
                throw new IllegalArgumentException("Không tìm thấy sinh viên với ID: " + studentId);
            }

            // Gán thesis cho sinh viên
            s.setThesisId(savedThesis);
            studentRepo.updateStudent(s);
        }

        return savedThesis;
    }

    @Override
    public Thesis updateThesis(int id, Map<String, String> payload) {
        Thesis t = thesisRepo.getThesisById(id);
        if (t == null) {
            throw new IllegalArgumentException("Không tìm thấy đề tài.");
        }
        String title = payload.get("title");
        String description = payload.get("description");
        String yearStr = payload.get("year");
        String board_id = payload.get("board_id");

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title không được để trống");
        }
        if (yearStr == null || yearStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Year không được để trống");
        }
        int year = Integer.parseInt(yearStr.trim());
        if (year <= 0) {
            throw new IllegalArgumentException("Year không được âm");
        }
        if (description != null) {
            t.setDescription(description);
        }
        if (board_id == null || board_id.trim().isEmpty()) {
            throw new IllegalArgumentException("BoardId không được để trống");
        }
        if (board_id != null) {
            int boardId = Integer.parseInt(board_id.trim());
            Board b = boardRepo.getBoardById(boardId);
            if (b == null) {
                throw new IllegalArgumentException("Board không tồn tại");
            }
            t.setBoardId(b);
        }
        t.setTitle(title.trim());
        t.setYear(year);
        return this.thesisRepo.addOrUpdate(t);
    }
}
