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
public class CriteriaServiceImpl implements CriteriaService{
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
        String name = payload.get("name");
        String maxScoreStr = payload.get("max_score");

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Tên tiêu chí không được để trống.");

        int maxScore;
        try {
            maxScore = Integer.parseInt(maxScoreStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Điểm tối đa không hợp lệ.");
        }

        if (maxScore <= 0)
            throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0.");

        Criteria c = new Criteria();
        c.setName(name);
        c.setMaxScore(maxScore);

        return repo.add(c);
    }

    @Override
    public Criteria update(int id, Map<String, String> payload) {
        Criteria c = repo.getById(id);
        if (c == null)
            throw new IllegalArgumentException("Không tìm thấy tiêu chí.");

        if (payload.containsKey("name")) {
            String name = payload.get("name");
            if (name == null || name.trim().isEmpty())
                throw new IllegalArgumentException("Tên tiêu chí không được để trống.");
            c.setName(name);
        }

        if (payload.containsKey("max_score")) {
            try {
                int maxScore = Integer.parseInt(payload.get("max_score"));
                if (maxScore <= 0)
                    throw new IllegalArgumentException("Điểm tối đa phải lớn hơn 0.");
                c.setMaxScore(maxScore);
            } catch (Exception e) {
                throw new IllegalArgumentException("Điểm tối đa không hợp lệ.");
            }
        }

        return repo.update(c);
    }

    @Override
    public void delete(int id) {
        Criteria c = repo.getById(id);
        if (c == null)
            throw new IllegalArgumentException("Không tìm thấy tiêu chí.");
        repo.delete(id);
    }
}
