/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisGrade;
import com.ts.repositories.StatsRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
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
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> getThesisStatsByMajorAndYear() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<Thesis> thesisRoot = cq.from(Thesis.class);
        Join<Thesis, Student> studentJoin = thesisRoot.join("studentSet"); // assuming mappedBy="thesis" in Student
        Join<Thesis, ThesisGrade> gradeJoin = thesisRoot.join("thesisGradeSet"); // assuming mappedBy="thesis" in ThesisGrade

        Path<String> majorPath = studentJoin.get("major");
        Path<Integer> yearPath = thesisRoot.get("year");
        Expression<Long> thesisCount = cb.countDistinct(thesisRoot.get("thesisId"));
        Expression<Double> avgScore = cb.avg(gradeJoin.get("score"));

        cq.multiselect(
                majorPath,
                yearPath,
                thesisCount,
                avgScore
        );
        cq.groupBy(majorPath, yearPath);

        return session.createQuery(cq).getResultList();
    }

}
