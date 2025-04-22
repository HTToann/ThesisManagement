/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Student;
import com.ts.repositories.StudentRepository;
import java.util.Map;
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
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void insertStudent(Student student) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(student);
        s.refresh(student);
    }

    @Override
    public Student updateStudent(Student s) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(s);
        return s;
    }

    @Override
    public Student getStudentByUserId(int userId) {
         Session session = this.factory.getObject().getCurrentSession();
         Student s = session.get(Student.class, userId);
         return s;
    }

    @Override
    public void deleleStudent(int userId) {
        Session session = this.factory.getObject().getCurrentSession();
           Student s = session.get(Student.class, userId);
           session.remove(s);
           
    }
}
