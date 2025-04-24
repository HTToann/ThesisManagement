/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.repositories.StatsRepository;
import com.ts.services.StatsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository statsRepo;

    @Override
    public List<Map<String, Object>> getStatsByMajorAndYear() {
        List<Object[]> results = statsRepo.getThesisStatsByMajorAndYear();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> record = new HashMap<>();
            record.put("major", row[0]);
            record.put("year", row[1]);
            record.put("totalTheses", row[2]);
            record.put("avgScore", row[3]);
            response.add(record);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> getGradesSummaryByBoardAndThesis(int boardId, int thesisId) {
            return this.statsRepo.getGradesSummaryByBoardAndThesis(boardId, thesisId);
    }
}
