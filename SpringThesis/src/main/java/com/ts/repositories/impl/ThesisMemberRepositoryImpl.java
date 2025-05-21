/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisLecturer;
import com.ts.pojo.ThesisMemberInfoDTO;
import com.ts.pojo.Users;
import com.ts.repositories.ThesisMemberRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
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
public class ThesisMemberRepositoryImpl implements ThesisMemberRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ThesisMemberInfoDTO> getThesisMembersByThesisId(int thesisId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ThesisMemberInfoDTO> lecturerQuery = cb.createQuery(ThesisMemberInfoDTO.class);
        Root<ThesisLecturer> lecturerRoot = lecturerQuery.from(ThesisLecturer.class);

        Join<ThesisLecturer, Users> lecturerUserJoin = lecturerRoot.join("users");
        Join<ThesisLecturer, Thesis> lecturerThesisJoin = lecturerRoot.join("thesis");

        lecturerQuery.select(cb.construct(
                ThesisMemberInfoDTO.class,
                lecturerUserJoin.get("userId"),
                lecturerUserJoin.get("firstName"),
                lecturerUserJoin.get("lastName"),
                lecturerUserJoin.get("role"),
                lecturerRoot.get("lectureRole"),
                lecturerThesisJoin.get("thesisId"),
                cb.literal(false) // isStudent = false
        ));

        lecturerQuery.where(cb.equal(lecturerThesisJoin.get("thesisId"), thesisId));
        List<ThesisMemberInfoDTO> lecturers = session.createQuery(lecturerQuery).getResultList();

        // --- Lấy sinh viên ---
        CriteriaQuery<ThesisMemberInfoDTO> studentQuery = cb.createQuery(ThesisMemberInfoDTO.class);
        Root<Student> studentRoot = studentQuery.from(Student.class);

        Join<Student, Users> studentUserJoin = studentRoot.join("userId");
        Join<Student, Thesis> studentThesisJoin = studentRoot.join("thesisId");

        studentQuery.select(cb.construct(
                ThesisMemberInfoDTO.class,
                studentUserJoin.get("userId"),
                studentUserJoin.get("firstName"),
                studentUserJoin.get("lastName"),
                studentUserJoin.get("role"),
                cb.nullLiteral(String.class), // lectureRole = null
                studentThesisJoin.get("thesisId"),
                cb.literal(true) // isStudent = true
        ));

        studentQuery.where(cb.equal(studentThesisJoin.get("thesisId"), thesisId));
        List<ThesisMemberInfoDTO> students = session.createQuery(studentQuery).getResultList();

        // Gộp lại kết quả
        List<ThesisMemberInfoDTO> allMembers = new ArrayList<>();
        allMembers.addAll(lecturers);
        allMembers.addAll(students);

        return allMembers;
    }

}
