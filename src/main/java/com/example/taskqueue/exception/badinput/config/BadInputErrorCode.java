package com.example.taskqueue.exception.badinput.config;

import com.example.taskqueue.exception.base.BaseErrorCode;

public enum BadInputErrorCode implements BaseErrorCode {

    TASK_LOOP_PRIORITY("TASK_01", "루프 태스크의 우선순위는 바꿀 수 없습니다."),
    TASK_ALL_DAY_PRIORITY("TASK_02", "일일 태스크의 우선순위는 바꿀 수 없습니다."),
    FOLLOW_USER_ID("FOLLOW_01", "유저와 팔로우 상대는 같을 수 없습니다."),
    NOT_EXIST_USER("FOLLOW_02", "존재하지 않는 유저입니다."),
    EXIST_FOLLOW("FOLLOW_03", "존재하는 팔로우 정보입니다.");


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
