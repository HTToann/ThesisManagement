/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Criteria;
import com.ts.repositories.CriteriaRepository;
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
public class CriteriaRepositoryImpl implements CriteriaRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Criteria> getAll() {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Criteria> cq = cb.createQuery(Criteria.class);
        Root<Criteria> root = cq.from(Criteria.class);
        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public Criteria getById(int id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(Criteria.class, id);
    }

    @Override
    public Criteria add(Criteria c) {
        Session session = factory.getObject().getCurrentSession();
        session.persist(c);
        return c;
    }

    @Override
    public Criteria update(Criteria c) {
        Session session = factory.getObject().getCurrentSession();
        session.merge(c);
        return c;
    }

    @Override
    public void delete(int id) {
        Session session = factory.getObject().getCurrentSession();
        Criteria c = session.get(Criteria.class, id);
        session.remove(c);
    }

    @Override
    public Criteria getByName(String name) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Criteria> cq = cb.createQuery(Criteria.class);
        Root<Criteria> root = cq.from(Criteria.class);

        cq.select(root).where(cb.equal(root.get("name"), name));

        List<Criteria> results = session.createQuery(cq).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

}
