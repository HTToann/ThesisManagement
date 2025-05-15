/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Student;
import com.ts.pojo.Users;
import com.ts.repositories.StudentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
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
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void insertStudent(Student student) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(student);
    }

    @Override
    public Student updateStudent(Student s) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(s);
        return s;
    }

    @Override
    public Student getStudentByUserId(int userId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);
        cq.select(root).where(cb.equal(root.get("userId").get("userId"), userId));
        return session.createQuery(cq).uniqueResult();
    }

    @Override
    public void deleleStudent(int id) {
//        Session session = factory.getObject().getCurrentSession();
//
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
//        Root<Student> root = cq.from(Student.class);
//
//        cq.select(root).where(cb.equal(root.get("users").get("userId"), userId));
//
//        Student s = session.createQuery(cq).uniqueResult();
        Session session = factory.getObject().getCurrentSession();
        Student s = this.getById(id);
        session.remove(s);

    }

    @Override
    public List<Student> getByThesisId(int thesisId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);

        cq.select(root).where(cb.equal(root.get("thesisId").get("thesisId"), thesisId));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Student getById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Student.class, id);
    }

    @Override
    public List<Student> getAll() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Student", Student.class);
        return q.getResultList();
    }

    @Override
    public List<Student> getStudentByUsername(String kw) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);
        cq.select(root).where(cb.like(cb.lower(root.get("userId").get("username")), "%" + kw.toLowerCase() + "%"));
        return session.createQuery(cq).getResultList();
    }
}
