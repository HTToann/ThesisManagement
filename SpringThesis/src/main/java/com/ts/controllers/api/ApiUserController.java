/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers.api;

//import com.ts.controllers.*;
import com.ts.pojo.Users;
import com.ts.services.UsersService;
import com.ts.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private UsersService usersService;

    @PostMapping(path = "/users/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestParam Map<String, String> params,
            @RequestParam(value = "avatar") MultipartFile avatar) {

        try {
            Users u = this.usersService.insertUser(params, avatar);
            return new ResponseEntity<>(u, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> auth(@RequestBody Users u) {
        if (this.usersService.authenticate(u.getUsername(), u.getPassword())) {
            Users user = usersService.getUserByUsername(u.getUsername());
            try {
                String token = JwtUtils.generateToken(user.getUsername(), user.getRole());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @RequestMapping("/secure/users/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Users> getProfile(Principal principal) {
        return new ResponseEntity<>(this.usersService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") int id) {
        try {
            Users u = usersService.getUserById(id);
            if (u == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User không tồn tại!"));
            }
            return ResponseEntity.ok(u);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }

    @PutMapping("/secure/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) {
        try {
            Users u = usersService.updateUser(id, params, avatar);
            return ResponseEntity.ok(u);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @GetMapping("/secure/users/get_role_lecturer")
    public ResponseEntity<?> getAllRoleLecturer(HttpServletRequest request) {
        String role = (String) request.getAttribute("role"); // Lấy role từ token đã validate trước đó

       if (!"ROLE_ADMIN".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }
        return ResponseEntity.ok(this.usersService.getAllUsersRoleLecturer());
    }

    @GetMapping("/secure/users/get_role_student")
    public ResponseEntity<?> getAllRoleStudent() {
        return ResponseEntity.ok(this.usersService.getAllUsersRoleStudent());
    }

    @DeleteMapping("/secure/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role"); // Lấy role từ token đã validate trước đó
        try {
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Bạn không có quyền!"));
            }

            this.usersService.deleteUsers(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }
}
