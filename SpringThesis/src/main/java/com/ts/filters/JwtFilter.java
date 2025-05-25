/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.filters;

import com.ts.pojo.Users;
import com.ts.services.UsersService;
import com.ts.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *

 */
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        if (uri.startsWith(String.format("%s/api/secure", httpRequest.getContextPath())) == true) {

            String header = httpRequest.getHeader("Authorization");

//            if (header == null || !header.startsWith("Bearer ")) {
//                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
//                return;
//            }
            if (header != null && header.startsWith("Bearer ")) {
                try {
                    // ✅ Luôn xóa context cũ (tránh giữ session từ login trước)
                    SecurityContextHolder.clearContext();
                    String token = header.substring(7);
                    String username = JwtUtils.validateTokenAndGetUsername(token);
                    String role = JwtUtils.getRoleFromToken(token);  // ➡️ Bạn cần viết thêm hàm lấy role

                    if (username != null) {
                        httpRequest.setAttribute("username", username);
                        // ⚠️ Đây là bước bạn còn thiếu: convert role -> authority
                        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                        System.out.println("Authorities = " + authorities);
                        // Ví dụ: role = "ROLE_MINISTRY"
//                        httpRequest.setAttribute("role", role); 
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        chain.doFilter(request, response);
                        return;
                    }
                } catch (Exception e) {
                    // Token sai
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc hết hạn");
                    return;
                }
            }
            // Nếu KHÔNG có token → thử kiểm tra session login (cho phép fallback admin login)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                chain.doFilter(request, response);
                return;
            }

            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Yêu cầu xác thực.");
            return;
        }

        chain.doFilter(request, response);
    }
}
