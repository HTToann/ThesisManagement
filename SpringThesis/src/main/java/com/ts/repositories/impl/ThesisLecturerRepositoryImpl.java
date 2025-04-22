/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisLecturer;
import com.ts.pojo.ThesisLecturerPK;
import com.ts.pojo.Users;
import com.ts.repositories.ThesisLecturerRepository;
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
public class ThesisLecturerRepositoryImpl implements ThesisLecturerRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void add(ThesisLecturer tl) {
        Session session = factory.getObject().getCurrentSession();
        session.persist(tl);
    }

    @Override
    public List<ThesisLecturer> getByThesisId(int thesisId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ThesisLecturer> cq = cb.createQuery(ThesisLecturer.class);
        Root<ThesisLecturer> root = cq.from(ThesisLecturer.class);
        cq.select(root).where(cb.equal(root.get("thesis").get("thesisId"), thesisId));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public void delete(int thesisId, int lecturerId) {
        Session session = factory.getObject().getCurrentSession();
        ThesisLecturerPK pk = new ThesisLecturerPK(thesisId, lecturerId);
        ThesisLecturer tl = session.get(ThesisLecturer.class, pk);
        if (tl != null) {
            session.remove(tl);
        }
    }

    @Override
    public void update(int thesisId, int oldLecturerId, int newLecturerId) {
        delete(thesisId, oldLecturerId);
        ThesisLecturer newTl = new ThesisLecturer();
        newTl.setThesis(new Thesis(thesisId));
        newTl.setUsers(new Users(newLecturerId));
        newTl.setThesisLecturerPK(new ThesisLecturerPK(thesisId, newLecturerId));
        add(newTl);
    }

    @Override
    public ThesisLecturer getByCompositeKey(int thesisId, int lecturerId) {
        Session session = factory.getObject().getCurrentSession();
        ThesisLecturerPK pk = new ThesisLecturerPK(thesisId, lecturerId);
        return session.get(ThesisLecturer.class, pk);
    }

}
