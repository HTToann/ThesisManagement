/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Faculty;
import com.ts.pojo.Users;
import com.ts.repositories.FacultyRepository;
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
public class FacultyRepositoryImpl implements FacultyRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Faculty getById(int id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(Faculty.class, id);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Faculty> cq = cb.createQuery(Faculty.class);
        Root<Faculty> root = cq.from(Faculty.class);
        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public Faculty addOrUpdate(Faculty f) {
        Session session = factory.getObject().getCurrentSession();
        if (f.getFacultyId() == null) {
            session.persist(f);
        } else {
            session.merge(f);
        }
        session.refresh(f);
        return f;
    }

    @Override
    public void delete(int id) {
        Session session = factory.getObject().getCurrentSession();
        Faculty faculty = session.get(Faculty.class, id);
        session.remove(faculty);
    }

    @Override
    public Faculty getByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();
        List<Faculty> result = s.createNamedQuery("Faculty.findByName", Faculty.class)
                .setParameter("name", name)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Faculty> searchByName(String keyword) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Faculty> cq = cb.createQuery(Faculty.class);
        Root<Faculty> root = cq.from(Faculty.class);
        cq.select(root).where(cb.like(root.get("name"), "%" + keyword + "%"));

        return session.createQuery(cq).getResultList();
    }

}
