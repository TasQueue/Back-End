package com.example.taskqueue.exception.badinput;

import com.example.taskqueue.exception.badinput.config.BadInputErrorCode;
import com.example.taskqueue.exception.badinput.config.BadInputException;
import com.example.taskqueue.exception.base.BaseErrorCode;

public class AllDayTaskPriorityBadInputException extends BadInputException {
    public AllDayTaskPriorityBadInputException() {
        super(BadInputErrorCode.TASK_ALL_DAY_PRIORITY);
    }
}
