/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Board;
import com.ts.pojo.BoardRequestDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface BoardService {
    List<Board> getAllBoard();
    Board addBoard(Board board);
    Board createBoardWithMembers(BoardRequestDTO request);
    Board updateBoard(int boardId,Map<String, String> params);
    Board getBoardById(int boardId);
}
