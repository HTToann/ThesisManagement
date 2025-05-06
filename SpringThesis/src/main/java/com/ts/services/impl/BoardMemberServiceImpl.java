/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.pojo.BoardMember;
import com.ts.pojo.BoardMemberPK;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisLecturer;
import com.ts.pojo.Users;
import com.ts.repositories.BoardMemberRepository;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.ThesisLecturerRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.BoardMemberService;
import com.ts.services.EmailService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class BoardMemberServiceImpl implements BoardMemberService {

    @Autowired
    private BoardMemberRepository repo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private UsersRepository usersRepo;
    @Autowired
    private ThesisLecturerRepository thesisLecturerRepo;
    @Autowired
    private ThesisRepository thesisRepo;
    @Autowired
    private EmailService emailService;

    private void sendGmailToMember(BoardMember member, String role, int boardId) {
        String email = member.getUsers().getEmail();
        String subject = "Thông báo phân công vào hội đồng phản biện";
        String content = String.format("""
        Xin chào %s %s,

        Bạn vừa được phân công làm %s trong hội đồng phản biện (Board ID: %d).

        Trân trọng,
        Hệ thống quản lý khóa luận.
        """,
                member.getUsers().getFirstName(),
                member.getUsers().getLastName(),
                role,
                boardId
        );

        emailService.sendEmail(email, subject, content);
    }

    @Override
    public void addBoardMember(BoardMember member) {
        int boardId = member.getBoard().getBoardId();
        int lecturerId = member.getUsers().getUserId();
        String role = member.getRoleInBoard();
        List<BoardMember> existingMembers = repo.getBoardMembersByBoardId(boardId);
        List<String> validRoles = List.of("ROLE_CHAIRMAIN", "ROLE_SECRETARY", "ROLE_COUNTER", "ROLE_MEMBERS");

        if (!validRoles.contains(role)) {
            throw new IllegalArgumentException("Vai trò không hợp lệ. Chỉ được chọn: ROLE_CHAIRMAIN, ROLE_SECRETARY, ROLE_COUNTER, ROLE_MEMBERS.");
        }
        if (existingMembers.size() >= 5) {
            throw new IllegalArgumentException("Hội đồng đã đủ 5 thành viên.");
        }

        for (BoardMember bm : existingMembers) {
            if (bm.getUsers().getUserId().equals(lecturerId)) {
                throw new IllegalArgumentException("Giảng viên này đã có trong hội đồng.");
            }
            if (bm.getRoleInBoard().equalsIgnoreCase(role)) {
                throw new IllegalArgumentException("Vai trò '" + role + "' đã được sử dụng.");
            }
        }
        List<Thesis> theses = thesisRepo.getThesesByBoardId(boardId);
        for (Thesis thesis : theses) {
            List<ThesisLecturer> lecturers = thesisLecturerRepo.getByThesisId(thesis.getThesisId());
            for (ThesisLecturer tl : lecturers) {
                if (tl.getUsers().getUserId().equals(lecturerId)) {
                    throw new IllegalArgumentException("Giảng viên này đang hướng dẫn một đề tài trong hội đồng, không thể là thành viên hội đồng.");
                }
            }
        }

        repo.addBoardMember(member);
        sendGmailToMember(member, role, boardId);
//        // Kiểm tra lại tổng số thành viên sau khi thêm (đảm bảo tối thiểu 3)
//        List<BoardMember> updatedMembers = repo.getBoardMembersByBoardId(boardId);
//        if (updatedMembers.size() < 3) {
//            throw new IllegalArgumentException("Hội đồng phải có ít nhất 3 thành viên.");
//        }
        // ✅ Gửi mail trước khi thêm (có thể đặt sau nếu cần đảm bảo persist thành công rồi mới gửi)
//        String email = member.getUsers().getEmail();
//        String subject = "Thông báo phân công vào hội đồng phản biện";
//        String content = String.format("""
//        Xin chào %s %s,
//
//        Bạn vừa được phân công làm %s trong hội đồng phản biện (Board ID: %d).
//
//        Trân trọng,
//        Hệ thống quản lý khóa luận.
//        """,
//                member.getUsers().getFirstName(),
//                member.getUsers().getLastName(),
//                role,
//                boardId
//        );

//        emailService.sendEmail(email, subject, content);
    }

    @Override
    public List<BoardMember> getBoardMembersByBoardId(int boardId) {
        if (this.repo.getBoardMembersByBoardId(boardId) != null) {
            return this.repo.getBoardMembersByBoardId(boardId);
        } else {
            throw new IllegalArgumentException("Hội đồng này không tồn tại.");
        }
    }

    @Override
    public void removeBoardMember(int boardId, int lecturerId) {
        BoardMemberPK pk = new BoardMemberPK(boardId, lecturerId);
        BoardMember member = repo.getById(pk);
        if (member == null) {
            throw new IllegalArgumentException("Không tìm thấy giảng viên này trong hội đồng.");
        }
        this.repo.removeBoardMember(boardId, lecturerId);
    }

    @Override
    public BoardMember add(Map<String, String> payload) {
        String boardIdStr = payload.get("boardId");
        String lecturerIdStr = payload.get("lecturerId");
        String role = payload.get("roleInBoard");

        int boardId = Integer.parseInt(boardIdStr.trim());
        int lecturerId = Integer.parseInt(lecturerIdStr.trim());

        // Lấy thực thể Board và Users từ ID
        Board board = boardRepo.getBoardById(boardId);
        Users lecturer = usersRepo.getUserById(lecturerId);
        if (board == null) {
            throw new IllegalArgumentException("Không tồn tại boardId ");
        }
        if (lecturer == null) {
            throw new IllegalArgumentException("Không tồn tại lectureId ");
        }
        // Tạo đối tượng BoardMember
        BoardMember member = new BoardMember();
        member.setBoard(board);
        member.setUsers(lecturer);
        member.setRoleInBoard(role.trim());
        member.setBoardMemberPK(new BoardMemberPK(boardId, lecturerId));
        this.addBoardMember(member);
        return member;
    }

    @Override
    public List<BoardMember> getAll() {
        return this.repo.getAll();
    }

    @Override
    public void updateRole(int boardId, int lecturerId, Map<String, String> payload) {
        String newRole = payload.get("roleInBoard");
        if (newRole == null || newRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Vai trò không được để trống.");
        }
        repo.updateBoardMemberRole(boardId, lecturerId, newRole);
    }
}
