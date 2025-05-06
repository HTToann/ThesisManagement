/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Board;
import com.ts.repositories.BoardRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lenovo
 */
@Repository
@Transactional
public class BoardRepositoryImpl implements BoardRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Board addBoard(Board board) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(board);
        return board;
    }

    @Override
    public List<Board> getAllBoard() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Board",Board.class);
        return q.getResultList();
    }

    @Override
    public Board updateBoard(Board board) {
         Session s = this.factory.getObject().getCurrentSession();
         if (board.getBoardId()!= null)
             s.merge(board);
         return board;
    }

    @Override
    public Board getBoardById(int boardId) {
         Session s = this.factory.getObject().getCurrentSession();
         return s.get(Board.class, boardId);
    }
    
}
