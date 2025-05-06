/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.BoardMember;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface BoardMemberService {
    void addBoardMember(BoardMember member);
    List<BoardMember> getAll();
    BoardMember add(Map<String,String> payload);
    List<BoardMember> getBoardMembersByBoardId(int boardId);
    void removeBoardMember(int boardId,int lectureId);
    void updateRole(int boardId, int lecturerId,Map<String, String> payload);
}
