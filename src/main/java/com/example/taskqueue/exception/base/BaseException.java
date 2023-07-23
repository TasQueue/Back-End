package com.example.taskqueue.exception.base;

public abstract class BaseException extends RuntimeException {

    private BaseErrorCode errorCode;

    public BaseErrorCode getErrorCode() {
        return errorCode;
    }

    public BaseException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
