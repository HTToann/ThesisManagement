/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.BoardMember;
import com.ts.pojo.BoardMemberPK;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface BoardMemberRepository {
    void addBoardMember(BoardMember member);
    List<BoardMember> getBoardMembersByBoardId(int boardId);
    void removeBoardMember(int boardId,int lectureId);
    BoardMember getById(BoardMemberPK pk);
}
