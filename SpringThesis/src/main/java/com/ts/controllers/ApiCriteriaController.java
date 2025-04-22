/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Criteria;
import com.ts.services.CriteriaService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/criteria")
@CrossOrigin
public class ApiCriteriaController {
    
    @Autowired
    private CriteriaService criteriaService;
    // GET /api/criteria
    @GetMapping
    public ResponseEntity<?> getAllCriteria() {
        return ResponseEntity.ok(criteriaService.getAll());
    }

    // POST /api/criteria
    @PostMapping
    public ResponseEntity<?> createCriteria(@RequestBody Map<String, String> payload) {
        try {
            Criteria c = criteriaService.add(payload);
            return new ResponseEntity<>(c, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/criteria/:id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCriteria(@PathVariable("id") int id, @RequestBody Map<String, String> payload) {
        try {
            Criteria updated = criteriaService.update(id, payload);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/criteria/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCriteria(@PathVariable("id") int id) {
        try {
            criteriaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
