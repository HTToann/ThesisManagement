/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Criteria;
import com.ts.repositories.CriteriaRepository;
import com.ts.services.CriteriaService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class CriteriaServiceImpl implements CriteriaService {

    @Autowired
    private CriteriaRepository repo;

    @Override
    public List<Criteria> getAll() {
        return repo.getAll();
    }

    @Override
    public Criteria getById(int id) {
        return repo.getById(id);
    }

    @Override
    public Criteria add(Map<String, String> payload) {
        String nameStr = payload.get("name");
        String maxScoreStr = payload.get("max_score");

        if (nameStr == null || nameStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tiêu chí không được để trống.");
        }

        // Kiểm tra tiêu chí trùng tên
        String name = nameStr.trim();
        Criteria existing = repo.getByName(name);
        if (existing != null) {
            throw new IllegalArgumentException("Tiêu chí với tên '" + name + "' đã tồn tại.");
        }

        int maxScore;
        try {
            maxScore = Integer.parseInt(maxScoreStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("maxScore phải là một số nguyên hợp lệ.");
        }
        if (maxScore > 10) {
            throw new IllegalArgumentException("Điểm tối đa không được lớn hơn 10.");
        }
        if (maxScore <= 0) {
            throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0.");
        }

        Criteria c = new Criteria();
        c.setName(name);
        c.setMaxScore(maxScore);

        return repo.add(c);
    }

    @Override
    public Criteria update(int id, Map<String, String> payload) {
        Criteria c = repo.getById(id);
        if (c == null) {
            throw new IllegalArgumentException("Không tìm thấy tiêu chí.");
        }

        if (payload.containsKey("name")) {
            String name = payload.get("name");
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên tiêu chí không được để trống.");
            }
            c.setName(name.trim());
        }

        if (payload.containsKey("max_score")) {
            try {
                int maxScore = Integer.parseInt(payload.get("max_score").trim());
                if (maxScore <= 0) {
                    throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0.");
                }
                if (maxScore > 10) {
                    throw new IllegalArgumentException("Điểm tối đa không được lớn hơn 10.");
                }
                c.setMaxScore(maxScore);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Điểm tối đa không hợp lệ.");
            }
        }

        return repo.update(c);
    }

    @Override
    public void delete(int id) {
        Criteria c = repo.getById(id);
        if (c == null) {
            throw new IllegalArgumentException("Không tìm thấy tiêu chí.");
        }
        repo.delete(id);
    }

    @Override
    public Criteria getByName(String name) {
        return this.repo.getByName(name);
    }

    @Override
    public List<Criteria> searchByName(String keyword) {
        return this.repo.searchByName(keyword);
    }
}
