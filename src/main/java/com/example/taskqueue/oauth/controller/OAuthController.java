package com.example.taskqueue.oauth.controller;


import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.oauth.service.CustomOAuth2UserService;
import com.example.taskqueue.oauth.service.LogoutService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class OAuthController {
    private final LogoutService logoutService;
    private final CustomOAuth2UserService customOAuth2UserService;
    @PostMapping("/kakao-logout")
    public String logout(){
        String accessToken = customOAuth2UserService.getAccessToken();
        User createdUser = customOAuth2UserService.getCreatedUser();
        System.out.println("accessToken 받아왔겠지?? = " + accessToken);
        System.out.println("createdUser.getEmail() = " + createdUser.getEmail());
        logoutService.logoutUser(accessToken,createdUser);
        return "redirect:/";
    }
    @GetMapping("/user-info")
    @ResponseBody
    public String getUserInfo(@CurrentUser User user) {
        if (user != null) {
            return "Hello, " + user.toString();
        } else {
            return "You are not logged in.";
        }
    }
}
