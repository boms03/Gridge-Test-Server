package com.example.demo.common.jwt;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtService.getJwt(request);
        boolean isTokenValid = jwtService.validateToken(request);

        if (isTokenValid) {
            this.setAuthentication(accessToken,request);
        }

        filterChain.doFilter(request, response);

    }

    private void setAuthentication(String token, HttpServletRequest request) {
        Long userId = jwtService.getUserId(request);
        String role = jwtService.getRole(request);


        User user = User.builder()
                .id(userId)
                .role(Constant.UserRole.valueOf(role))
                .name("TEMP")
                .build();

        CustomUserDetail customUserDetails = new CustomUserDetail(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/swagger-ui", "/api-docs", "/auth", "/login/kakao", "/auth/KAKAO/login", "/favicon.ico"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
