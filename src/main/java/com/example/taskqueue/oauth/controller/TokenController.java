package com.example.taskqueue.oauth.controller;

import com.example.taskqueue.oauth.jwt.JwtService;
import com.example.taskqueue.security.filter.JwtAuthenticationProcessingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    private final JwtService jwtService;
    @PostMapping("/refreshtoken/get")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        try {
            // 리프레시 토큰을 쿠키에서 추출
            Cookie[] cookies = request.getCookies();
            String refreshToken = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refreshToken")) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // 리프레시 토큰을 검증하고, 유효하다면 새로운 액세스 토큰을 생성하여 응답으로 보냄
            if (jwtService.isTokenValid(refreshToken)) {
                jwtAuthenticationProcessingFilter.checkRefreshTokenAndReIssueAccessToken(response,refreshToken);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/ex")
    public String example(){
        return "example pushed";
    }
}
