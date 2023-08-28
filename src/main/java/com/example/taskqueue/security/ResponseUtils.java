package com.example.taskqueue.security;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResponseUtils {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, BaseErrorCode errorCode) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(Map.of())
                .build();
        System.out.println("ResponseUtils 의 errorResponse = " + errorResponse);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    public void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Map<String, String> successBody) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        objectMapper.writeValue(response.getOutputStream(), successBody);
    }

    @Transactional
    public void setUserTokens(User user, String refreshToken) {
        userRepository.findByEmail(user.getEmail())
                .ifPresentOrElse(
                        existingToken -> existingToken.updateRefreshToken(refreshToken),
                        () -> userRepository.save(user)
                );
    }
}
