package com.example.taskqueue.exception.notfound.config;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exception.base.BaseException;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
