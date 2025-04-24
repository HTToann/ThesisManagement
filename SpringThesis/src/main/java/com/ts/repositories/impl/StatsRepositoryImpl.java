/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories.impl;

import com.ts.pojo.Board;
import com.ts.pojo.BoardMember;
import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisGrade;
import com.ts.pojo.Users;
import com.ts.repositories.StatsRepository;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public List<Map<String, Object>> getGradesSummaryByBoardAndThesis(int boardId, int thesisId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<ThesisGrade> root = cq.from(ThesisGrade.class);
        Join<ThesisGrade, Users> lecturerJoin = root.join("users");
        Join<ThesisGrade, Board> boardJoin = root.join("board");
        Join<Users, BoardMember> boardMemberJoin = lecturerJoin.join("boardMemberSet", JoinType.LEFT);

        cq.multiselect(
                lecturerJoin.get("firstName").alias("firstName"),
                lecturerJoin.get("lastName").alias("lastName"),
                cb.avg(root.get("score")).alias("avgScore"),
                boardMemberJoin.get("roleInBoard").alias("roleInBoard")
        );

        Predicate thesisPredicate = cb.equal(root.get("thesis").get("thesisId"), thesisId);
        Predicate boardPredicate = cb.equal(boardJoin.get("boardId"), boardId);
        Predicate memberBoardPredicate = cb.equal(boardMemberJoin.get("board").get("boardId"), boardId);
        cq.where(cb.and(thesisPredicate, boardPredicate, memberBoardPredicate));

        cq.groupBy(
                lecturerJoin.get("userId"),
                lecturerJoin.get("firstName"),
                lecturerJoin.get("lastName"),
                boardMemberJoin.get("roleInBoard")
        );

        List<Tuple> results = session.createQuery(cq).getResultList();

        List<Map<String, Object>> summaries = new ArrayList<>();
        for (Tuple tuple : results) {
            Map<String, Object> row = new HashMap<>();
            row.put("lecturerName", tuple.get("firstName") + " " + tuple.get("lastName"));
            row.put("avgScore", tuple.get("avgScore"));
            row.put("role", tuple.get("roleInBoard"));
            summaries.add(row);
        }

        return summaries;
    }

}
