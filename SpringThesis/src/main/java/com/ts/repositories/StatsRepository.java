/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface StatsRepository {
     List<Object[]> getThesisStatsByMajorAndYear();
     List<Map<String, Object>> getGradesSummaryByBoardAndThesis(int boardId, int thesisId);
}
