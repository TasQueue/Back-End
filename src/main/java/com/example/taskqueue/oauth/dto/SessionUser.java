package com.example.taskqueue.oauth.dto;

import com.example.taskqueue.oauth.CustomOAuth2User;
import lombok.Getter;

@Getter
public class SessionUser {
    private String name;
    private String email;
    public SessionUser(CustomOAuth2User oAuth2User) {
        this.name = oAuth2User.getName();
        this.email = oAuth2User.getEmail();
    }
}
