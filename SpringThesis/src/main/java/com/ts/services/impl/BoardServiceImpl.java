/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.repositories.BoardRepository;
import com.ts.services.BoardService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardRepository boardRepo;
    @Override
    public List<Board> getAllBoard() {
        return boardRepo.getAllBoard();
    }

    @Override
    public Board addBoard(Board board) {
        return boardRepo.addBoard(board);
    }

    @Override
    public Board updateBoard(int boardId,Map<String, String> params){
        Board b = this.boardRepo.getBoardById(boardId);
        if( b == null ) {
            throw new IllegalArgumentException("Không tìm thấy board có id = " + boardId);
        }
        String isLocked=params.get("isLocked");
        if (isLocked.toLowerCase().trim().equals("true")){
             b.setIsLocked(Boolean.TRUE);
        }
        return this.boardRepo.updateBoard(b);
    }
    @Override
    public Board getBoardById(int boardId) {
        return boardRepo.getBoardById(boardId);
    }
    
}
