/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface PdfExportService {
     byte[] exportStatsToPdf(List<Map<String, Object>> statsData);
     byte[] exportBoardSummaryToPdf(List<Map<String, Object>> summaries, int year, String currentDate,int boardID,String title,String semester,Double score);
}
