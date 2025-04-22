/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Board;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface BoardRepository {
    List<Board> getAllBoard();
    Board addBoard(Board board);
    Board updateBoard(Board board);
    Board getBoardById(int boardId);
}
