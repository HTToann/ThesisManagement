/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.BoardMember;
import com.ts.pojo.BoardMemberPK;
import com.ts.repositories.BoardMemberRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
public class BoardMemberRepositoryImpl implements BoardMemberRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addBoardMember(BoardMember member) {
        Session session = factory.getObject().getCurrentSession();
        session.persist(member);
    }

    @Override
    public List<BoardMember> getBoardMembersByBoardId(int boardId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<BoardMember> cq = cb.createQuery(BoardMember.class);
        Root<BoardMember> root = cq.from(BoardMember.class);
        cq.select(root).where(cb.equal(root.get("board").get("boardId"), boardId));
        return session.createQuery(cq).getResultList();
    }

    @Override
    public void removeBoardMember(int boardId, int lectureId) {
        Session session = factory.getObject().getCurrentSession();
        BoardMemberPK pk = new BoardMemberPK(boardId, lectureId);
        BoardMember member = session.get(BoardMember.class, pk);
        if (member != null) {
            session.remove(member);
        }
    }

    @Override
    public BoardMember getById(BoardMemberPK pk) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(BoardMember.class, pk);
    }

}
