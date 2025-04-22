/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.ThesisGrade;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.CriteriaRepository;
import com.ts.repositories.ThesisGradeRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.ThesisGradeService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class ThesisGradeServiceImpl implements ThesisGradeService{
    @Autowired
    private ThesisGradeRepository repo;
    @Autowired 
    private UsersRepository userRepo;
    @Autowired 
    private ThesisRepository thesisRepo;
    @Autowired 
    private BoardRepository boardRepo;
    @Autowired
    private CriteriaRepository criteriaRepo;

    @Override
    public void add(Map<String, String> payload) {
        int boardId = Integer.parseInt(payload.get("board_id"));
        int thesisId = Integer.parseInt(payload.get("thesis_id"));
        int lecturerId = Integer.parseInt(payload.get("lecturer_id"));
        int criteriaId = Integer.parseInt(payload.get("criteria_id"));
        double score = Double.parseDouble(payload.get("score"));

        ThesisGrade tg = new ThesisGrade();
        tg.setBoard(boardRepo.getBoardById(boardId));
        tg.setThesis(thesisRepo.getThesisById(thesisId));
        tg.setUsers(userRepo.getUserById(lecturerId));
        tg.setCriteria(criteriaRepo.getById(criteriaId));
        tg.setScore(score);

        repo.add(tg);
    }

    @Override
    public void update(Map<String, String> payload) {
        int boardId = Integer.parseInt(payload.get("board_id"));
        int thesisId = Integer.parseInt(payload.get("thesis_id"));
        int lecturerId = Integer.parseInt(payload.get("lecturer_id"));
        int criteriaId = Integer.parseInt(payload.get("criteria_id"));
        double score = Double.parseDouble(payload.get("score"));

        ThesisGrade tg = repo.getByCompositeKey(boardId, thesisId, lecturerId, criteriaId);
        if (tg == null)
            throw new IllegalArgumentException("Không tồn tại bản ghi để cập nhật.");

        tg.setScore(score);
        repo.update(tg);
    }

    @Override
    public List<ThesisGrade> getByThesisId(int thesisId) {
        return repo.getByThesisId(thesisId);
    }

    @Override
    public List<ThesisGrade> getByLecturerAndBoard(int lecturerId, int boardId) {
        return repo.getByLecturerAndBoard(lecturerId, boardId);
    }
}
