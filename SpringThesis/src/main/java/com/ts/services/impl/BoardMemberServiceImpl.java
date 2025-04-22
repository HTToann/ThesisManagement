/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.BoardMember;
import com.ts.pojo.BoardMemberPK;
import com.ts.repositories.BoardMemberRepository;
import com.ts.services.BoardMemberService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class BoardMemberServiceImpl implements BoardMemberService {

    @Autowired
    private BoardMemberRepository repo;

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
}
