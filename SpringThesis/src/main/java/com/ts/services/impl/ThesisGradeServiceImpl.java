/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.pojo.Criteria;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisGrade;
import com.ts.pojo.ThesisGradePK;
import com.ts.pojo.Users;
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
public class ThesisGradeServiceImpl implements ThesisGradeService {

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
        int boardId = Integer.parseInt(payload.get("board_id").trim());
        int thesisId = Integer.parseInt(payload.get("thesis_id").trim());
        int lecturerId = Integer.parseInt(payload.get("lecturer_id").trim());
        int criteriaId = Integer.parseInt(payload.get("criteria_id").trim());
        double score = Double.parseDouble(payload.get("score"));
        Board board = boardRepo.getBoardById(boardId);

        checkIfBoardLocked(board);

        Criteria criteria = criteriaRepo.getById(criteriaId);
        if (criteria == null) {
            throw new IllegalArgumentException("Không tồn tại tiêu chí với ID = " + criteriaId);
        }
        if (score > criteria.getMaxScore()) {
            throw new IllegalArgumentException("Điểm không được vượt quá điểm tối đa: " + criteria.getMaxScore());
        }
        if (board == null) {
            throw new IllegalArgumentException("Không tồn tại hội đồng với ID = " + boardId);
        }

        Thesis thesis = thesisRepo.getThesisById(thesisId);
        if (thesis == null) {
            throw new IllegalArgumentException("Không tồn tại đề tài với ID = " + thesisId);
        }

        Users lecturer = userRepo.getUserById(lecturerId);
        if (lecturer == null) {
            throw new IllegalArgumentException("Không tồn tại giảng viên với ID = " + lecturerId);
        }

        ThesisGrade existing = repo.getByCompositeKey(boardId, thesisId, lecturerId, criteriaId);
        if (existing != null) {
            throw new IllegalArgumentException("Bản ghi điểm đã tồn tại. Vui lòng sử dụng chức năng cập nhật.");
        }
        ThesisGrade tg = new ThesisGrade();
        tg.setThesisGradePK(new ThesisGradePK(boardId, thesisId, lecturerId, criteriaId));
        tg.setBoard(boardRepo.getBoardById(boardId));
        tg.setThesis(thesisRepo.getThesisById(thesisId));
        tg.setUsers(userRepo.getUserById(lecturerId));
        tg.setCriteria(criteriaRepo.getById(criteriaId));
        tg.setScore(score);

        repo.add(tg);
    }

    @Override
    public void update(Map<String, String> payload) {
        int boardId = Integer.parseInt(payload.get("board_id").trim());
        int thesisId = Integer.parseInt(payload.get("thesis_id").trim());
        int lecturerId = Integer.parseInt(payload.get("lecturer_id").trim());
        int criteriaId = Integer.parseInt(payload.get("criteria_id").trim());
        double score = Double.parseDouble(payload.get("score").trim());

        Board board = boardRepo.getBoardById(boardId);
        checkIfBoardLocked(board);

        Criteria criteria = criteriaRepo.getById(criteriaId);
        if (criteria == null) {
            throw new IllegalArgumentException("Không tồn tại tiêu chí với ID = " + criteriaId);
        }
        if (score > criteria.getMaxScore()) {
            throw new IllegalArgumentException("Điểm không được vượt quá điểm tối đa: " + criteria.getMaxScore());
        }
        if (board == null) {
            throw new IllegalArgumentException("Không tồn tại hội đồng với ID = " + boardId);
        }

        Thesis thesis = thesisRepo.getThesisById(thesisId);
        if (thesis == null) {
            throw new IllegalArgumentException("Không tồn tại đề tài với ID = " + thesisId);
        }

        Users lecturer = userRepo.getUserById(lecturerId);
        if (lecturer == null) {
            throw new IllegalArgumentException("Không tồn tại giảng viên với ID = " + lecturerId);
        }

        ThesisGrade tg = repo.getByCompositeKey(boardId, thesisId, lecturerId, criteriaId);
        if (tg == null) {
            throw new IllegalArgumentException("Không tồn tại bản ghi để cập nhật.");
        }

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

    @Override
    public void delete(Map<String, String> payload) {
        int boardId = Integer.parseInt(payload.get("board_id").trim());
        int thesisId = Integer.parseInt(payload.get("thesis_id").trim());
        int lecturerId = Integer.parseInt(payload.get("lecturer_id").trim());
        int criteriaId = Integer.parseInt(payload.get("criteria_id").trim());

        ThesisGrade tg = repo.getByCompositeKey(boardId, thesisId, lecturerId, criteriaId);
        if (tg == null) {
            throw new IllegalArgumentException("Không tồn tại bản ghi để xóa.");
        }

        // Optional: kiểm tra nếu board bị khóa thì không cho xóa
        Board board = boardRepo.getBoardById(boardId);
        checkIfBoardLocked(board);

        repo.delete(boardId, thesisId, lecturerId, criteriaId);
    }

    private void checkIfBoardLocked(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Hội đồng không tồn tại.");
        }
        if (Boolean.TRUE.equals(board.getIsLocked())) {
            throw new IllegalStateException("Hội đồng đã bị khóa, không thể thao tác.");
        }
    }
}
