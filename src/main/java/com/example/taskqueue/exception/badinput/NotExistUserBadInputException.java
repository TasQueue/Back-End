package com.example.taskqueue.exception.badinput;

import com.example.taskqueue.exception.badinput.config.BadInputErrorCode;
import com.example.taskqueue.exception.badinput.config.BadInputException;

public class NotExistUserBadInputException extends BadInputException {
    public NotExistUserBadInputException() {
        super(BadInputErrorCode.NOT_EXIST_USER);
    }
}
