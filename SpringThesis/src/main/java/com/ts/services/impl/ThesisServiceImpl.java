/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Board;
import com.ts.pojo.Thesis;
import com.ts.repositories.BoardRepository;
import com.ts.repositories.ThesisRepository;
import com.ts.services.ThesisService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class ThesisServiceImpl implements ThesisService {

    @Autowired
    private ThesisRepository thesisRepo;
    @Autowired
    private BoardRepository boardRepo;

    @Override
    public Thesis getThesisById(int thesisId) {
        return this.thesisRepo.getThesisById(thesisId);
    }

    @Override
    public void deleteThesisById(int thesisId) {
        if (this.thesisRepo.getThesisById(thesisId) != null) {
            this.deleteThesisById(thesisId);
        } else {
            throw new IllegalArgumentException("Không tồn tại thesisId");
        }
    }

    @Override
    public List<Thesis> getAllThesis() {
        return this.thesisRepo.getAllThesis();
    }

    @Override
    public Thesis addThesis(Map<String, String> payload) {
        Thesis t = new Thesis();

        String title = payload.get("title");
        String description = payload.get("description");
        String yearStr = payload.get("year");
        int year = Integer.parseInt(yearStr);
        String board_id = payload.get("board_id");
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title không được để trống");
        }
        if (year <= 0) {
            throw new IllegalArgumentException("Year không được âm");
        }
        if (description != null) {
            t.setDescription(description);
        }
        if (board_id != null) {
            int id = Integer.parseInt(board_id);
            Board b = boardRepo.getBoardById(id);
            if (b == null) {
                throw new IllegalArgumentException("Board không tồn tại");
            }
            t.setBoardId(b);
        }
        t.setTitle(title);
        t.setYear(year);
        return this.thesisRepo.addOrUpdate(t);
    }

    @Override
    public Thesis updateThesis(int id, Map<String, String> payload) {
        Thesis t = thesisRepo.getThesisById(id);
        if (t == null) {
            throw new IllegalArgumentException("Không tìm thấy đề tài.");
        }
        String title = payload.get("title");
        String description = payload.get("description");
        String yearStr = payload.get("year");
        String board_id = payload.get("board_id");

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title không được để trống");
        }
        int year = Integer.parseInt(yearStr);
        if (year <= 0) {
            throw new IllegalArgumentException("Year không được âm");
        }
        if (description != null) {
            t.setDescription(description);
        }
        if (board_id != null) {
            int boardId = Integer.parseInt(board_id);
            Board b = boardRepo.getBoardById(boardId);
            if (b == null) {
                throw new IllegalArgumentException("Board không tồn tại");
            }
            t.setBoardId(b);
        }
        t.setTitle(title);
        t.setYear(year);
        return this.thesisRepo.addOrUpdate(t);
    }
}
