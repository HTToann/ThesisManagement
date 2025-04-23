/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.ThesisGrade;
import com.ts.pojo.ThesisGradePK;
import com.ts.repositories.ThesisGradeRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
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
public class ThesisGradeRepositoryImpl implements ThesisGradeRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void add(ThesisGrade tg) {
        factory.getObject().getCurrentSession().persist(tg);
    }

    @Override
    public void update(ThesisGrade tg) {
        factory.getObject().getCurrentSession().merge(tg);
    }

    @Override
    public List<ThesisGrade> getByThesisId(int thesisId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ThesisGrade> cq = cb.createQuery(ThesisGrade.class);
        Root<ThesisGrade> root = cq.from(ThesisGrade.class);
        cq.select(root).where(cb.equal(root.get("thesis").get("thesisId"), thesisId));
        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<ThesisGrade> getByLecturerAndBoard(int lecturerId, int boardId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ThesisGrade> cq = cb.createQuery(ThesisGrade.class);
        Root<ThesisGrade> root = cq.from(ThesisGrade.class);
        cq.select(root).where(
                cb.and(
                        cb.equal(root.get("users").get("userId"), lecturerId),
                        cb.equal(root.get("board").get("boardId"), boardId)
                )
        );
        return session.createQuery(cq).getResultList();
    }

    @Override
    public ThesisGrade getByCompositeKey(int boardId, int thesisId, int lecturerId, int criteriaId) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(ThesisGrade.class, new ThesisGradePK(boardId, thesisId, lecturerId, criteriaId));
    }
    @Override
    public void delete(int boardId, int thesisId, int lecturerId, int criteriaId) {
        Session session = factory.getObject().getCurrentSession();
        ThesisGradePK pk = new ThesisGradePK(boardId, thesisId, lecturerId, criteriaId);
        ThesisGrade tg = session.get(ThesisGrade.class, pk);
        session.remove(tg);
    }
}
