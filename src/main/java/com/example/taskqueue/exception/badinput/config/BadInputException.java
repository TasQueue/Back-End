package com.example.taskqueue.exception.badinput.config;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exception.base.BaseException;
import lombok.Getter;

import java.util.Map;

@Getter
public class BadInputException extends BaseException {

    private Map<String, String> badInputMaps = Map.of();
    public BadInputException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public BadInputException(BaseErrorCode errorCode, Map<String, String> badInputMaps) {
        super(errorCode);
        this.badInputMaps = badInputMaps;
    }
}
