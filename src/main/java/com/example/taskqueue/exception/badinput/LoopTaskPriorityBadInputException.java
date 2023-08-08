package com.example.taskqueue.exception.badinput;

import com.example.taskqueue.exception.badinput.config.BadInputErrorCode;
import com.example.taskqueue.exception.badinput.config.BadInputException;

public class LoopTaskPriorityBadInputException extends BadInputException {
    public LoopTaskPriorityBadInputException() {
        super(BadInputErrorCode.TASK_LOOP_PRIORITY);
    }
}
