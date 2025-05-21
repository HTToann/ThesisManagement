/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisLecturer;
import com.ts.pojo.Users;
import com.ts.repositories.ThesisRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class ThesisRepositoryImpl implements ThesisRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Thesis getThesisById(int thesisId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Thesis.class, thesisId);
    }

    @Override
    public List<Thesis> getAllThesis() {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Thesis> cq = cb.createQuery(Thesis.class);
        Root<Thesis> root = cq.from(Thesis.class);
        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public Thesis addOrUpdate(Thesis thesis) {
        Session s = this.factory.getObject().getCurrentSession();
        if (thesis.getThesisId() == null) {
            s.persist(thesis);
        } else {
            s.merge(thesis);
        }
        s.refresh(thesis);
        return thesis;

    }

    @Override
    public void deleteThesisById(int thesisId) {
        Session s = this.factory.getObject().getCurrentSession();
        Thesis t = this.getThesisById(thesisId);
        s.remove(t);
    }

    @Override
    public List<Thesis> getThesesByBoardId(int boardId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Thesis> cq = cb.createQuery(Thesis.class);
        Root<Thesis> root = cq.from(Thesis.class);

        cq.select(root).where(cb.equal(root.get("boardId").get("boardId"), boardId));

        Query<Thesis> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Thesis> getThesisByName(String kw) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Thesis> cq = cb.createQuery(Thesis.class);
        Root<Thesis> root = cq.from(Thesis.class);
        cq.select(root).where(cb.like(cb.lower(root.get("title")), "%" + kw.toLowerCase() + "%"));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Thesis> getThesesByUserId(int userId) {
        Session session = this.factory.getObject().getCurrentSession();

        // --- Query 1: user là giảng viên ---
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Thesis> lecturerQuery = cb.createQuery(Thesis.class);
        Root<ThesisLecturer> lecturerRoot = lecturerQuery.from(ThesisLecturer.class);
        Join<ThesisLecturer, Thesis> thesisJoinLecturer = lecturerRoot.join("thesis");
        Join<ThesisLecturer, Users> userJoinLecturer = lecturerRoot.join("users");

        lecturerQuery.select(thesisJoinLecturer)
                .where(cb.equal(userJoinLecturer.get("userId"), userId));

        List<Thesis> lecturerResults = session.createQuery(lecturerQuery).getResultList();

        // --- Query 2: user là sinh viên ---
        CriteriaQuery<Thesis> studentQuery = cb.createQuery(Thesis.class);
        Root<Student> studentRoot = studentQuery.from(Student.class);
        Join<Student, Users> userJoinStudent = studentRoot.join("userId");
        Join<Student, Thesis> thesisJoinStudent = studentRoot.join("thesisId");

        studentQuery.select(thesisJoinStudent)
                .where(cb.equal(userJoinStudent.get("userId"), userId));

        List<Thesis> studentResults = session.createQuery(studentQuery).getResultList();

        // --- Gộp & loại trùng ---
        Set<Thesis> combined = new HashSet<>();
        combined.addAll(lecturerResults);
        combined.addAll(studentResults);

        return new ArrayList<>(combined);
    }
}
