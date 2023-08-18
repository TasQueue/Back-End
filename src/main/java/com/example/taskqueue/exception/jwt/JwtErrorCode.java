package com.example.taskqueue.exception.jwt;

import com.example.taskqueue.exception.base.BaseErrorCode;

public enum JwtErrorCode implements BaseErrorCode {
    JWT_EXPIRED("JWT_01", "토큰 값이 만료되었습니다."),
    JWT_INVALID("JWT_02", "토큰 형식이 올바르지 않습니다."),
    JWT_VERIFICATION("JWT_03","토큰이 일치하지 않습니다.");

    private String code;
    private String message;

    JwtErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
