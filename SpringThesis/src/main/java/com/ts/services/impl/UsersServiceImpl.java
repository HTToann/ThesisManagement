/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ts.pojo.Faculty;
import com.ts.pojo.Major;
import com.ts.pojo.Student;
import com.ts.pojo.Users;
import com.ts.repositories.FacultyRepository;
import com.ts.repositories.MajorRepository;
import com.ts.repositories.StudentRepository;
import com.ts.repositories.UsersRepository;
import com.ts.services.UsersService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lenovo
 */
@Service("userDetailsService")
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository repo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private MajorRepository majorRepo;
    @Autowired
    private FacultyRepository falRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Users getUserByUsername(String username) {
        return this.repo.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public Users insertUser(Map<String, String> params, MultipartFile avatar) {
        Users u = new Users();


        String firstName = params.get("firstName");
        String lastName = params.get("lastName");
        String username = params.get("username");
        String password = params.get("password");
        String phone = params.get("phone");
        String email = params.get("email");
        String role = params.get("role");
        String facultyIdStr = params.get("facultyId");
        String majorIdStr = params.get("majorId");


        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        if (this.repo.getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        if (password == null || password.trim().length() < 6) {
            throw new IllegalArgumentException("Password không hợp lệ (>= 6 ký tự)");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name không được để trống");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name không được để trống");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone không được để trống");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role không được để trống");
        }

        
        List<String> validRoles = List.of("ROLE_ADMIN", "ROLE_MINISTRY", "ROLE_LECTURER", "ROLE_STUDENT");
        if (!validRoles.contains(role.toUpperCase())) {
            throw new IllegalArgumentException("Role không hợp lệ");
        }

        
        if (facultyIdStr == null || facultyIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("FacultyId không được trống");
        }
        int facultyId = Integer.parseInt(facultyIdStr.trim());
        Faculty f = this.falRepo.getById(facultyId);
        if (f == null) {
            throw new IllegalArgumentException("Faculty không tồn tại");
        }

        // --- Nếu là STUDENT thì validate major trước khi tạo user ---
        Major selectedMajor = null;
        if (role.equalsIgnoreCase("ROLE_STUDENT")) {
            if (majorIdStr == null || majorIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("MajorId không được để trống");
            }
            int majorId = Integer.parseInt(majorIdStr.trim());
            selectedMajor = this.majorRepo.getById(majorId);
            if (selectedMajor == null) {
                throw new IllegalArgumentException("Major không tồn tại");
            }
        }

        // --- Upload avatar ---
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UsersServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("Lỗi khi tải ảnh đại diện");
            }
        }

        // --- Set user fields ---
        u.setFirstName(firstName.trim());
        u.setLastName(lastName.trim());
        u.setUsername(username.trim());
        u.setPassword(this.passwordEncoder.encode(password));
        u.setPhone(phone.trim());
        u.setEmail(email.trim());
        u.setRole(role.trim());
        u.setFacultyId(f);

        Users savedUser = this.repo.addOrUpdate(u);

        // --- Create Student nếu là ROLE_STUDENT ---
        if (role.equalsIgnoreCase("ROLE_STUDENT")) {
            Student s = new Student();
            s.setUserId(savedUser);
            s.setMajorSet(Set.of(selectedMajor));
            this.studentRepo.insertStudent(s);
        }

        return savedUser;
    }

    @Override
    public boolean authenticate(String username, String password
    ) {
        return this.repo.authenticate(username, password);
    }

    @Override
    public Users getUserById(int id
    ) {
        return this.repo.getUserById(id);
    }

    @Override
    public Users updateUser(int id, Map<String, String> params,
            MultipartFile avatar
    ) {
        Users u = this.repo.getUserById(id);

        if (u == null) {
            throw new IllegalArgumentException("Không tìm thấy user có id = " + id);
        }
        if (params.containsKey("firstName")) {
            String fn = params.get("firstName");
            u.setFirstName(fn != null ? fn.trim() : null);
        }

        if (params.containsKey("lastName")) {
            String ln = params.get("lastName");
            u.setLastName(ln != null ? ln.trim() : null);
        }

        if (params.containsKey("password")) {
            String password = params.get("password");
            if (password == null || password.trim().length() < 6) {
                throw new IllegalArgumentException("Password không được có độ dài < 6");
            }
            u.setPassword(this.passwordEncoder.encode(password.trim()));
        }

        if (params.containsKey("phone")) {
            String phone = params.get("phone");
            u.setPhone(phone != null ? phone.trim() : null);
        }

        if (params.containsKey("email")) {
            String email = params.get("email");
            u.setEmail(email != null ? email.trim() : null);
        }

        if (params.containsKey("facultyId")) {
            String facultyIdStr = params.get("facultyId");
            if (facultyIdStr == null || facultyIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("FacultyId không được để trống");
            }
            try {
                int facultyId = Integer.parseInt(facultyIdStr.trim());
                Faculty f = this.falRepo.getById(facultyId);
                if (f == null) {
                    throw new IllegalArgumentException("Faculty không tồn tại");
                }
                u.setFacultyId(f);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("FacultyId không hợp lệ");
            }
        }
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UsersServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        if (params.containsKey("active")) {
//            u.setActive(Boolean.parseBoolean(params.get("active")));
//        }
        // Gọi lại hàm repository
        return this.repo.addOrUpdate(u);
    }

    @Override
    public List<Users> getAllUsersRoleLecturer() {
        return this.repo.getAllUsersRoleLecturer();
    }

    @Override
    public List<Users> getAllUsersRoleStudent() {
        return this.repo.getAllUsersRoleStudent();
    }

    @Override
    public void deleteUsers(int id) {
        this.repo.deleteUsers(id);
    }

    @Override
    public List<Users> getAllUsers() {
        return this.repo.getAllUsers();
    }

}
