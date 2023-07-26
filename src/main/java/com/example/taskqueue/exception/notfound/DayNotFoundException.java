package com.example.taskqueue.exception.notfound;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundException;

public class DayNotFoundException extends ResourceNotFoundException {
    public DayNotFoundException() {
        super(ResourceNotFoundErrorCode.DAY_NOT_FOUND);
    }
}
