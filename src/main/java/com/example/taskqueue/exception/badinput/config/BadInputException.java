package com.example.taskqueue.exception.badinput.config;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exception.base.BaseException;

public class BadInputException extends BaseException {
    public BadInputException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
