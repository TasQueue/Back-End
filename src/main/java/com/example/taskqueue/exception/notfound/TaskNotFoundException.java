package com.example.taskqueue.exception.notfound;

import com.example.taskqueue.exception.notfound.config.ResourceNotFoundErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {
    public TaskNotFoundException() {
        super(ResourceNotFoundErrorCode.TASK_NOT_FOUND);
    }
}
