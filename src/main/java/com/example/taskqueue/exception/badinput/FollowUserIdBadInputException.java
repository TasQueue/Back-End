package com.example.taskqueue.exception.badinput;

import com.example.taskqueue.exception.badinput.config.BadInputErrorCode;
import com.example.taskqueue.exception.badinput.config.BadInputException;

public class FollowUserIdBadInputException extends BadInputException {
    public FollowUserIdBadInputException() {
        super(BadInputErrorCode.FOLLOW_USER_ID);
    }

}
