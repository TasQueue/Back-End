package com.example.taskqueue.common.dto;

import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.CompleteState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import java.time.LocalTime;

@Getter
public class SimpleTaskDto {

    @ApiModelProperty(value = "태스크 아이디 값", example = "1")
    private Long id;

    @ApiModelProperty(value = "태스크 이름", example = "수학 과제")
    private String name;

    @ApiModelProperty(value = "태스크 우선순위", example = "1")
    private int priority;

    @ApiModelProperty(value = "태스크 시작 시간", example = "13:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime startTime;

    @ApiModelProperty(value = "태스크 종료 시간", example = "14:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endTime;

    @ApiModelProperty(value = "시간정보가 필요한지 여부", example = "true")
    private boolean requiredTime;

    @ApiModelProperty(value = "루프 태스크 여부", example = "YES/NO")
    private String repeatState;

    @ApiModelProperty(value = "달력 표기 여부", example = "YES/NO")
    private String calenderState;

    @ApiModelProperty(value = "완료 여부", example = "YES/NO")
    private String completeState;

    public SimpleTaskDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.priority = task.getPriority();
        this.startTime = task.getStartTime().toLocalTime();
        this.endTime = task.getEndTime().toLocalTime();
        this.requiredTime = task.getRequiredTime();

        if(task.getRepeatState().equals(RepeatState.NO)) {
            this.repeatState = "NO";
        } else {
            this.repeatState = "YES";
        }

        if(task.getCalenderState().equals(CalenderState.NO)) {
            this.calenderState = "NO";
        } else {
            this.calenderState = "YES";
        }

        if(task.getCompleteState().equals(CompleteState.NO)) {
            this.completeState = "NO";
        } else {
            this.completeState = "YES";
        }
    }

}
