package com.example.taskqueue.config;

import com.example.taskqueue.oauth.currentUser.CurrentUserArgumentResolver;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserArgumentResolver(userRepository,httpSession));
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("https?://localhost:\\d+") //원래는 * 이었음
                .allowCredentials(true)
                .exposedHeaders("Authorization")
                .maxAge(3000);
        //첫줄 주석처리 후 true로 바꿈
    }
}
