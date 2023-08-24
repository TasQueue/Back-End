package com.example.taskqueue.oauth.controller;


import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.oauth.jwt.JwtService;
import com.example.taskqueue.oauth.service.CustomOAuth2UserService;
import com.example.taskqueue.oauth.service.LogoutService;
import com.example.taskqueue.user.controller.dto.response.GetUserDto;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "OAuth API")
@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final LogoutService logoutService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtService jwtService;



    @ApiOperation(
            value = "소셜로그인 로그아웃",
            notes = "로그아웃한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT")
    })
    @PostMapping("/kakao-logout")
    public String logout(){
        String accessToken = customOAuth2UserService.getAccessToken();
        User createdUser = customOAuth2UserService.getCreatedUser();
        String JWTAccessToken = jwtService.getJWTAccessToken();
        System.out.println("accessToken 받아왔겠지?? = " + accessToken);
        System.out.println("JWTAccessToken = " + JWTAccessToken);
        //System.out.println("createdUser.getEmail() = " + createdUser.getEmail());
        if (accessToken == null && JWTAccessToken == null) {
            return "로그인을 먼저 해주세요";
        } else {
            logoutService.logoutUser(accessToken,JWTAccessToken,createdUser);
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
}
