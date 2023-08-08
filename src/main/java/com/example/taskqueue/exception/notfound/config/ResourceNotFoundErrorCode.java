package com.example.taskqueue.exception.notfound.config;

import com.example.taskqueue.exception.base.BaseErrorCode;

public enum ResourceNotFoundErrorCode implements BaseErrorCode {


    TASK_NOT_FOUND("TASK_00", "존재하지 않는 태스크 정보입니다."),
    FOLLOW_NOT_FOUND("FOLLOW_00", "존재하지 않는 팔로우 정보입니다."),
    CATEGORY_NOT_FOUND("CATEGORY_00", "존재하지 않는 카테고리 정보입니다."),
    USER_NOT_FOUND("USER_00", "존재하지 않는 유저 정보입니다."),
    DAY_NOT_FOUND("DAY_00", "존재하지 않는 요일 정보입니다.");

    private String code;
    private String message;

    ResourceNotFoundErrorCode(String code, String message) {
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
