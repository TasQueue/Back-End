package com.example.taskqueue.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {

    private String code;

    private String message;

    private Map<String, String> errors;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

}
