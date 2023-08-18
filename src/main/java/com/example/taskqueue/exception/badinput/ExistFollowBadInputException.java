package com.example.taskqueue.exception.badinput;

import com.example.taskqueue.exception.badinput.config.BadInputErrorCode;
import com.example.taskqueue.exception.badinput.config.BadInputException;

public class ExistFollowBadInputException extends BadInputException {
    public ExistFollowBadInputException() {
        super(BadInputErrorCode.EXIST_FOLLOW);
    }
}
