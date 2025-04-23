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
}
