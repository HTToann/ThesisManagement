/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Users;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lenovo
 */
public interface UsersService extends UserDetailsService{
    Users getUserByUsername(String username);
    Users getUserById(int id);
    Users insertUser(Map<String,String> params,MultipartFile avatar);
    Users updateUser(int id, Map<String, String> params, MultipartFile avatar);
    List<Users> getAllUsersRoleLecturer();
    List<Users> getAllUsersRoleStudent();
    void deleteUsers(int id);
    boolean authenticate(String username,String password);
}
