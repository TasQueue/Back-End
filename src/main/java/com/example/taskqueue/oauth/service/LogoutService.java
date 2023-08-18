package com.example.taskqueue.oauth.service;

import com.example.taskqueue.oauth.jwt.JwtService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    public void logoutUser(String accessToken, String JWTAccessToken, User createdUser) {
        String logoutUrl = "https://kapi.kakao.com/v1/user/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseLogout = restTemplate.exchange(logoutUrl, HttpMethod.POST, entity, String.class);

        if (responseLogout.getStatusCode().is2xxSuccessful()) {
            // 로그아웃 성공 처리
            System.out.println("accessToken으로 로그아웃 성공 " + accessToken);
            System.out.println("로그아웃된 사용자 social ID = " + responseLogout.getBody());
            jwtService.expireAccessToken(JWTAccessToken);

            User deleteUser = userRepository.findByEmail(createdUser.getEmail()).orElse(null);
            System.out.println("deleteUser = " + deleteUser.getEmail());
            if(deleteUser.getEmail().equals(createdUser.getEmail())){
                deleteUser.updateRefreshToken(null);
                userRepository.save(deleteUser);
            }else {
                System.out.println("해당 사용자 없음");
            }
        } else {
            // 로그아웃 실패 처리
            System.out.println("accessToken으로 로그아웃 실패 " + accessToken);
        }
    }
}
