package com.example.taskqueue.exception.notfound;

import com.example.taskqueue.exception.notfound.config.ResourceNotFoundErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundException;

public class FollowNotFoundException extends ResourceNotFoundException {

    public FollowNotFoundException() {
        super(ResourceNotFoundErrorCode.FOLLOW_NOT_FOUND);
    }

}
