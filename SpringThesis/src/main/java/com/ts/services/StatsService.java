/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface StatsService {
    List<Map<String, Object>> getStatsByMajorAndYear();
}
