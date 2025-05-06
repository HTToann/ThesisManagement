/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Board;
import com.ts.pojo.Major;
import com.ts.pojo.Users;
import com.ts.repositories.MajorRepository;
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
public class MajorRepositoryImpl implements MajorRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Major getById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Major.class, id);
    }

    @Override
    public Major getByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();
        List<Major> result = s.createNamedQuery("Major.findByName", Major.class)
                .setParameter("name", name)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Major> getAllMajors() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Major", Major.class);
        return q.getResultList();
    }

    @Override
    public Major addOrUpdate(Major major) {
        Session s = this.factory.getObject().getCurrentSession();
        if (major.getMajorId() == null) {
            s.persist(major);
        } else {
            s.merge(major);
        }
        s.flush();
        s.refresh(major);

        return major;
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Major m = this.getById(id);
        s.remove(m);
    }

    @Override
    public List<Major> searchByName(String keyword) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Major> cq = cb.createQuery(Major.class);
        Root<Major> root = cq.from(Major.class);
        cq.select(root).where(cb.like(root.get("name"), "%" + keyword + "%"));

        return s.createQuery(cq).getResultList();
    }

}
