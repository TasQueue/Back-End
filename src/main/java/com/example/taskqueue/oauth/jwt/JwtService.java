package com.example.taskqueue.oauth.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.taskqueue.exception.jwt.JwtErrorCode;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.redis.RedisUtil;
import com.example.taskqueue.security.ResponseUtils;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private int accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private int refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final ResponseUtils responseUtils;
    private final RedisUtil redisUtil;
    private String JWTAccessToken;
    private String JWTRefreshToken;

    public void expireAccessToken(String accessToken){
        redisUtil.setBlackList(accessToken, "accessToken", 5);
    }

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email) {
        System.out.println("JWT Access Token 생성");
        Date now = new Date();
        JWTAccessToken = JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

                //클레임으로 email을 사용함
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
        return JWTAccessToken;
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken() {
        Date now = new Date();
        JWTRefreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
        return JWTRefreshToken;
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
        //이 부분에서 http 헤더에 쿠키를 만들고 토큰들을 실어서 보낸다.
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        System.out.println("헤더에서 리프레시 토큰 추출");
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();
        }
        return Optional.empty();
//        return Optional.ofNullable(request.getHeader(refreshHeader))
//                .filter(refreshToken -> refreshToken.startsWith(BEARER))
//                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        System.out.println("헤더에서 액세스 토큰 추출");
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(HttpServletRequest request, HttpServletResponse response, String accessToken) throws IOException {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(EMAIL_CLAIM) // claim(Email) 가져오기
                    .asString());
        } catch (Exception e) {
            log.error("Access Token Expired");
            responseUtils.setErrorResponse(response, HttpStatus.UNAUTHORIZED, JwtErrorCode.JWT_EXPIRED);
            return Optional.empty();
        }
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(3600) // 쿠키 만료 시간 설정 (초 단위)
                .httpOnly(true) // JavaScript에서 쿠키에 접근하지 못하도록 설정
                // SameSite 설정
                .sameSite("None")
                // Secure 설정 (HTTPS 연결일 때만 전송)
                .secure(true)
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken) {
        Optional<User> result = userRepository.findByEmail(email);

        if(result.isPresent()){
            result.get().updateRefreshToken(refreshToken);
            userRepository.save(result.get());
        }
    }

    public boolean isTokenValid(String token) {
        try {
            if (redisUtil.hasKeyBlackList(token)){
                // 에러 발생시키는 부분 수정
                log.error("로그아웃 이미 했으므로 해당 액세스토큰을 사용할 수 없음.");
//                Map<String, String> errorMap = new HashMap<>();
//                errorMap.put(token,"로그아웃 처리된 사용자입니다.");
//
//                ErrorResponse errorResponse = new ErrorResponse("UNAUTHORIZED", "Unauthorized", errorMap);
//                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
                return false;
            }
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(e.getMessage(), "유효하지 않은 토큰입니다. {}");

            ErrorResponse errorResponse = new ErrorResponse("UNAUTHORIZED", "Unauthorized", errorMap);
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}