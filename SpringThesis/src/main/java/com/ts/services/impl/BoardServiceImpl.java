/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisGrade;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.ThesisGradeRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.services.BoardService;
import com.ts.services.EmailService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private ThesisGradeRepository thesisGradeRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Board> getAllBoard() {
        return boardRepo.getAllBoard();
    }

    @Override
    public Board addBoard(Board board) {
        return boardRepo.addBoard(board);
    }

    @Override
    public Board updateBoard(int boardId, Map<String, String> params) {
        Board b = this.boardRepo.getBoardById(boardId);
        if (b == null) {
            throw new IllegalArgumentException("Không tìm thấy board có id = " + boardId);
        }
        String isLocked = params.get("isLocked").trim();
        if (isLocked.toUpperCase().trim().equals("TRUE")) {
            b.setIsLocked(Boolean.TRUE);
            // 🔒 Khóa tất cả các đề tài thuộc hội đồng này
            List<Thesis> theses = thesisRepo.getThesesByBoardId(boardId);
            for (Thesis t : theses) {
                t.setIsLocked(Boolean.TRUE);
                thesisRepo.addOrUpdate(t); // Gọi update để lưu lại
                // 🔍 Tìm student thuộc thesis này
                Student student = studentRepo.getByThesisId(t.getThesisId());
                if (student != null && student.getUserId() != null) {
                    String email = student.getUserId().getEmail();

                    // 📊 Tính điểm trung bình từ thesis_grade
                    List<ThesisGrade> grades = thesisGradeRepo.getByThesisId(t.getThesisId());
                    double avg = grades.stream().mapToDouble(ThesisGrade::getScore).average().orElse(0.0);

                    // ✉️ Gửi email thông báo điểm
                    String subject = "Thông báo điểm khoá luận tốt nghiệp";
                    String content = String.format("""
                    Xin chào %s %s,

                    Hội đồng đã hoàn tất chấm điểm đề tài: %s.
                    Điểm trung bình của bạn là: %.2f

                    Trân trọng,
                    Hệ thống quản lý khóa luận.
                    """, student.getUserId().getFirstName(), student.getUserId().getLastName(),
                            t.getTitle(), avg
                    );

                    emailService.sendEmail(email, subject, content);
                }
            }
        }
        if (isLocked.toUpperCase().trim().equals("FALSE")) {
            b.setIsLocked(Boolean.FALSE);
            // 🔒 Khóa tất cả các đề tài thuộc hội đồng này
            List<Thesis> theses = thesisRepo.getThesesByBoardId(boardId);
            for (Thesis t : theses) {
                t.setIsLocked(Boolean.FALSE);
                thesisRepo.addOrUpdate(t); // Gọi update để lưu lại
            }
        }
        return this.boardRepo.updateBoard(b);
    }

    @Override
    public Board getBoardById(int boardId) {
        return boardRepo.getBoardById(boardId);
    }

}
