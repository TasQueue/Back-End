package com.example.taskqueue.oauth.controller;


import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.exception.jwt.JwtErrorCode;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.oauth.handler.OAuth2LoginSuccessHandler;
import com.example.taskqueue.oauth.jwt.JwtService;
import com.example.taskqueue.oauth.request.KakaoAuthRequest;
import com.example.taskqueue.oauth.service.CustomOAuth2UserService;
import com.example.taskqueue.oauth.service.LogoutService;
import com.example.taskqueue.security.ResponseUtils;
import com.example.taskqueue.user.controller.dto.response.GetUserDto;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "OAuth API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {
    private final LogoutService logoutService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtService jwtService;
    private final ResponseUtils responseUtils;


    @ApiOperation(
            value = "소셜로그인 로그아웃",
            notes = "로그아웃한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT")
    })
    @PostMapping("/kakao-logout")
    public String logout() {
        String accessToken = customOAuth2UserService.getAccessToken();
        User createdUser = customOAuth2UserService.getCreatedUser();
        String JWTAccessToken = jwtService.getJWTAccessToken();
        System.out.println("accessToken 받아왔겠지?? = " + accessToken);
        System.out.println("JWTAccessToken = " + JWTAccessToken);
        //System.out.println("createdUser.getEmail() = " + createdUser.getEmail());
        if (accessToken == null && JWTAccessToken == null) {
            return "로그인을 먼저 해주세요";
        } else {
            logoutService.logoutUser(accessToken, JWTAccessToken, createdUser);
            return "로그아웃 되었음";
        }
    }


    @ApiOperation(
            value = "자신의 소셜로그인 정보 확인하기.",
            notes = "자신의 소셜로그인 정보를 확인한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @GetMapping("/user-info")
    public String getUserInfo(@ApiIgnore @CurrentUser User user) {
        System.out.println("user = " + user.toString());
        if (user != null) {
            return "Hello, " + user.toString();
        } else {
            return "Please Log in first!";
        }
    }

    @GetMapping("/kakao-login")
    public void redirectToKakaoAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/kakao");
    }

    @PostMapping("/oauth/get")
    public void getOauthToken(
            @org.springframework.web.bind.annotation.RequestBody KakaoAuthRequest kakaoAuthRequest,
            HttpServletResponse response
    ) throws IOException {
        System.out.println("일단 oauth get에 들어옴");
        try {
            String JWTAccessToken = jwtService.getJWTAccessToken();
            String JWTRefreshToken = jwtService.getJWTRefreshToken();
            // 리프레시 토큰을 쿠키에 추가

            jwtService.sendAccessAndRefreshToken(response,JWTAccessToken,JWTRefreshToken);
        } catch (Exception e) {
            responseUtils.setErrorResponse(response, HttpStatus.UNAUTHORIZED, JwtErrorCode.JWT_VERIFICATION);
        }
    }
}
