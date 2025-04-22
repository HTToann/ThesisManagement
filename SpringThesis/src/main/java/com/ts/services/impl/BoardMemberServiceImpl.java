/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.pojo.BoardMember;
import com.ts.pojo.BoardMemberPK;
import com.ts.pojo.Users;
import com.ts.repositories.BoardMemberRepository;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.BoardMemberService;
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

    @Override
    public void addBoardMember(BoardMember member) {
        int boardId = member.getBoard().getBoardId();
        int lecturerId = member.getUsers().getUserId();
        String role = member.getRoleInBoard();
        List<BoardMember> existingMembers = repo.getBoardMembersByBoardId(boardId);

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

        repo.addBoardMember(member);
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
        BoardMember member = repo.getById(pk); // bạn có thể viết hàm getById ở repo
        if (member == null) {
            throw new IllegalArgumentException("Không tìm thấy giảng viên này trong hội đồng.");
        }
        this.repo.removeBoardMember(boardId, lecturerId);
    }

    @Override
    public BoardMember add(Map<String, String> payload) {
            String boardIdStr=payload.get("boardId");
            String lecturerIdStr=payload.get("lecturerId");
            String role = payload.get("roleInBoard");
        
            int boardId = Integer.parseInt(boardIdStr);
            int lecturerId = Integer.parseInt(lecturerIdStr);

            // Lấy thực thể Board và Users từ ID
            Board board = boardRepo.getBoardById(boardId);
            Users lecturer = usersRepo.getUserById(lecturerId);
            if(board == null )
                throw new IllegalArgumentException("Không tồn tại boardId ");
            if(lecturer == null)
                throw new IllegalArgumentException("Không tồn tại lectureId ");
            // Tạo đối tượng BoardMember
            BoardMember member = new BoardMember();
            member.setBoard(board);
            member.setUsers(lecturer);
            member.setRoleInBoard(role);
            member.setBoardMemberPK(new BoardMemberPK(boardId, lecturerId));
            this.addBoardMember(member);
            return member;
    }

   
}
