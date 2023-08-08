package com.example.taskqueue.oauth.controller;


import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "OAuth API")
@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final LogoutService logoutService;
    private final CustomOAuth2UserService customOAuth2UserService;



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
        System.out.println("accessToken 받아왔겠지?? = " + accessToken);
        System.out.println("createdUser.getEmail() = " + createdUser.getEmail());
        logoutService.logoutUser(accessToken,createdUser);
        return "redirect:/";
    }


    @ApiOperation(
            value = "자신의 소셜로그인 정보 확인하기.",
            notes = "자신의 소셜로그인 정보를 확인한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = String.class),
    })
    @GetMapping("/user-info")
    public String getUserInfo(@ApiIgnore @CurrentUser User user) {
        if (user != null) {
            return "Hello, " + user.toString();
        } else {
            return "You are not logged in.";
        }
    }
}
