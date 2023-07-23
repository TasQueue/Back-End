package com.example.taskqueue.exception.badinput.config;

import com.example.taskqueue.exception.base.BaseErrorCode;

public enum BadInputErrorCode implements BaseErrorCode {

    TASK_TIME_REVERSE("TASK_01", "잘못된 형식의 태스크 수행시간 입력입니다.");


    private String code;
    private String message;

    BadInputErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
