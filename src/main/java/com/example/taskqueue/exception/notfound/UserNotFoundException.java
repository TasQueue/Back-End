package com.example.taskqueue.exception.notfound;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super(ResourceNotFoundErrorCode.USER_NOT_FOUND);
    }
}
