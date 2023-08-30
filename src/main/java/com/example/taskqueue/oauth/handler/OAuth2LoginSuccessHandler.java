package com.example.taskqueue.oauth.handler;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exception.jwt.JwtErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundErrorCode;
import com.example.taskqueue.oauth.CustomOAuth2User;
import com.example.taskqueue.oauth.jwt.JwtService;
import com.example.taskqueue.security.ResponseUtils;
import com.example.taskqueue.user.entity.Role;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import com.example.taskqueue.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ResponseUtils responseUtils;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2 Login 성공!");
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            System.out.println("oAuth2User = " + oAuth2User.toString());
            loginSuccess(response,oAuth2User);
        } catch (Exception e) {
            log.error("OAuth login 진행시 에러 발생 {}",e.getMessage());
            responseUtils.setErrorResponse(response, HttpStatus.UNAUTHORIZED, JwtErrorCode.JWT_VERIFICATION);
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        System.out.println("swagger 테스트용 accessToken = " + accessToken);
        //response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        //response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        //jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
