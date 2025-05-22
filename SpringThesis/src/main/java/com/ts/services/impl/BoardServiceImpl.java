/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.enumRole.BoardRole;
import com.ts.pojo.Board;
import com.ts.pojo.BoardMember;
import com.ts.pojo.BoardMemberPK;
import com.ts.pojo.BoardRequestDTO;
import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisGrade;
import com.ts.repositories.BoardMemberRepository;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.ThesisGradeRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.services.BoardService;
import com.ts.services.EmailService;
import java.util.ArrayList;
import java.util.HashSet;
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
    private BoardMemberRepository boardMemberRepo;
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

    private void sendGmailToStudent(Student student, Thesis t) {
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

    @Override
    public Board updateBoard(int boardId, Map<String, String> payload) {
        Board b = this.boardRepo.getBoardById(boardId);
        if (b == null) {
            throw new IllegalArgumentException("Không tìm thấy board có id = " + boardId);
        }
        String isLocked = payload.get("isLocked");
        if (isLocked != null && isLocked.trim().equalsIgnoreCase("true")) {
            b.setIsLocked(Boolean.TRUE);
            // 🔒 Khóa tất cả các đề tài thuộc hội đồng này
            List<Thesis> theses = thesisRepo.getThesesByBoardId(boardId);
            for (Thesis t : theses) {
//                t.setIsLocked(Boolean.TRUE);
//                thesisRepo.addOrUpdate(t); // Gọi update để lưu lại
                // 🔍 Tìm student thuộc thesis này
                // Cập nhật điểm cho khóa luận
                List<ThesisGrade> grades = thesisGradeRepo.getByThesisId(t.getThesisId());
                double avg = grades.stream().mapToDouble(ThesisGrade::getScore).average().orElse(0.0);
                t.setScore(avg);
                this.thesisRepo.addOrUpdate(t);
                List<Student> students = studentRepo.getByThesisId(t.getThesisId());
                for (Student student : students) {
                    if (student != null && student.getUserId() != null) {
                        sendGmailToStudent(student, t);
                    }
                }
            }
        } else if (isLocked != null && isLocked.trim().equalsIgnoreCase("false")) {
            b.setIsLocked(Boolean.FALSE);
            // 🔒 Khóa tất cả các đề tài thuộc hội đồng này
//            List<Thesis> theses = thesisRepo.getThesesByBoardId(boardId);
//            for (Thesis t : theses) {
//                t.setIsLocked(Boolean.FALSE);
//                thesisRepo.addOrUpdate(t); // Gọi update để lưu lại
//            }
        }
        return this.boardRepo.updateBoard(b);
    }

    @Override
    public Board getBoardById(int boardId) {
        return boardRepo.getBoardById(boardId);
    }

    @Override
    public Board createBoardWithMembers(BoardRequestDTO request) {
        Set<Integer> seenLecturerIds = new HashSet<>();
        Set<String> seenRoles = new HashSet<>();

        Set<String> uniqueRoles = Set.of(
                BoardRole.ROLE_CHAIRMAIN.name(),
                BoardRole.ROLE_SECRETARY.name(),
                BoardRole.ROLE_COUNTER.name()
        );

        if (request.getMembers().size() < 3) {
            throw new IllegalArgumentException("Hội đồng phải có ít nhất 3 thành viên.");
        }

        for (BoardRequestDTO.MemberRequest m : request.getMembers()) {
            if (!seenLecturerIds.add(m.getLecturerId())) {
                throw new IllegalArgumentException("Giảng viên ID " + m.getLecturerId() + " bị trùng trong danh sách.");
            }

            if (uniqueRoles.contains(m.getRole())) {
                if (!seenRoles.add(m.getRole())) {
                    throw new IllegalArgumentException("Vai trò '" + m.getRole() + "' đã được gán cho người khác.");
                }
            }
        }

        // Tạo hội đồng
        Board savedBoard = boardRepo.addBoard(new Board());

        // Gán thành viên
        for (BoardRequestDTO.MemberRequest m : request.getMembers()) {
            BoardMemberPK pk = new BoardMemberPK(savedBoard.getBoardId(), m.getLecturerId());
            BoardMember bm = new BoardMember(pk, m.getRole());
            bm.setBoard(savedBoard);
            boardMemberRepo.addBoardMember(bm);
        }

        return savedBoard;

    }
}
