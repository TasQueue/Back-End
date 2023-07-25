package com.example.taskqueue.oauth;

import com.example.taskqueue.user.entity.Role;
import com.example.taskqueue.user.entity.User;
import lombok.Builder;
import lombok.Getter;


import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    //private String provider;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        return ofKakao("id", attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttirbuteName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> account = (Map<String, Object>) response.get("profile");
        return OAuthAttributes.builder()
                .name((String) account.get("nickname"))
                .email((String)response.get("email"))
                //.provider("Kakao")
                .attributes(attributes)
                .nameAttributeKey(userNameAttirbuteName)
                .build();
    }
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
