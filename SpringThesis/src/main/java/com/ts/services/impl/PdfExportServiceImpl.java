/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.ts.services.PdfExportService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author Lenovo
 */
@Service
public class PdfExportServiceImpl implements PdfExportService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public byte[] exportStatsToPdf(List<Map<String, Object>> statsData) {
        Context context = new Context();
        context.setVariable("stats", statsData);

        // 2. Render HTML
        String html = templateEngine.process("stats-report", context);

        // 3. Chuyển HTML sang PDF bằng Flying Saucer
        ITextRenderer renderer = new ITextRenderer();

        try {
            // ✅ Font tiếng Việt
            renderer.getFontResolver().addFont(
                    getClass().getResource("/fonts/Roboto-VariableFont_wdth,wght.ttf").toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
        } catch (DocumentException ex) {
            Logger.getLogger(PdfExportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfExportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        renderer.setDocumentFromString(html);
        renderer.layout();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            renderer.createPDF(out);
            return out.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(PdfExportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public byte[] exportBoardSummaryToPdf(List<Map<String, Object>> summaries, int year, String currentDate, int boardID) {
        Context context = new Context();
        context.setVariable("summaries", summaries);
        context.setVariable("year", year);
        context.setVariable("currentDate", currentDate);
        context.setVariable("boardID", boardID);
        String htmlContent = templateEngine.process("board-summary-pdf", context);

        ITextRenderer renderer = new ITextRenderer();
        try {
            renderer.getFontResolver().addFont(
                    getClass().getResource("/fonts/Roboto-VariableFont_wdth,wght.ttf").toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
        } catch (DocumentException ex) {
            Logger.getLogger(PdfExportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfExportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(PdfExportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
