/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Users;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface UsersRepository {
    Users getUserByUsername(String username);
    List<Users> getAllUsers();
    List<Users> getAllUsersRoleLecturer();
    List<Users> getAllUsersRoleStudent();
    Users getUserById(int id);
    Users addOrUpdate(Users u);
    boolean authenticate(String username, String password);
    void deleteUsers(int id);
}
