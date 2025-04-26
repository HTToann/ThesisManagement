/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Users;
import com.ts.repositories.UsersRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lenovo
 */
@Repository
@Transactional
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Users getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        List<Users> result = s.createNamedQuery("Users.findByUsername", Users.class)
                .setParameter("username", username)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Users> getAllUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Users", Users.class);
        return q.getResultList();
    }

    @Override
    public Users getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Users.class, id);
    }

    @Override
    public Users addOrUpdate(Users u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u.getUserId() == null) {
            s.persist(u);
        } else {
            s.merge(u);
        }
        s.flush();
        s.refresh(u);

        return u;
    }

    @Override
    public void deleteUsers(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Users u = this.getUserById(id);
        s.remove(u);
    }

    @Override
    public boolean authenticate(String username, String password) {
        Users u = this.getUserByUsername(username);
        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public List<Users> getAllUsersRoleLecturer() {
        Session s = this.factory.getObject().getCurrentSession();

        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Users> query = builder.createQuery(Users.class);
        Root<Users> root = query.from(Users.class);
        query.select(root).where(builder.equal(root.get("role"), "ROLE_LECTURER"));

        Query<Users> q = s.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<Users> getAllUsersRoleStudent() {
        Session s = this.factory.getObject().getCurrentSession();

        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Users> query = builder.createQuery(Users.class);
        Root<Users> root = query.from(Users.class);
        query.select(root).where(builder.equal(root.get("role"), "ROLE_STUDENT"));

        Query<Users> q = s.createQuery(query);
        return q.getResultList();
    }
}
