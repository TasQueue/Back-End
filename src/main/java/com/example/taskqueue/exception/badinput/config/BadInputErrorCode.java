package com.example.taskqueue.exception.badinput.config;

import com.example.taskqueue.exception.base.BaseErrorCode;

public enum BadInputErrorCode implements BaseErrorCode {

    TASK_LOOP_PRIORITY("TASK_01", "루프 태스크의 우선순위는 바꿀 수 없습니다."),
    TASK_ALL_DAY_PRIORITY("TASK_02", "일일 태스크의 우선순위는 바꿀 수 없습니다.");


    private String code;
    private String message;

    BadInputErrorCode(String code, String message) {
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
