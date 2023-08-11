package com.example.taskqueue.task.controller.dto.response;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.common.dto.SimpleCategoryDto;
import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.AllDayState;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetTaskDto {

    @ApiModelProperty(value = "태스크 아이디 값", example = "1")
    private Long id;

    @ApiModelProperty(value = "태스크 이름", example = "수학 과제하기")
    private String name;

    @ApiModelProperty(value = "태스크 요일 정보", example = "[MON, TUE, WED, THU]")
    private List<String> dayOfWeek;

    @ApiModelProperty(value = "태스크 소유 유저 정보")
    @JsonProperty("user")
    private SimpleUserDto simpleUserDto;

    @ApiModelProperty(value = "태스크 카테고리 정보")
    @JsonProperty("category")
    private SimpleCategoryDto simpleCategoryDto;

    @ApiModelProperty(value = "태스크 시작시간 정보", example = "2023-08-08 13:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "태스크 종료시간 정보", example = "2023-08-08 14:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "시간 정보가 필요한지 여부", example = "true")
    private boolean requiredTime;

    @ApiModelProperty(value = "종일 태스크 여부", example = "YES/NO")
    private String allDayState;

    @ApiModelProperty(value = "루프 태스크 여부", example = "YES/NO")
    private String repeatState;

    @ApiModelProperty(value = "달력 표기 여부", example = "YES/NO")
    private String calenderState;

    public GetTaskDto(Task task, User user, List<String> dayOfWeek, Category category) {
        this.id = task.getId();
        this.name = task.getName();
        this.dayOfWeek = dayOfWeek;
        this.simpleUserDto = new SimpleUserDto(user);
        this.simpleCategoryDto = new SimpleCategoryDto(category);
        this.startTime = task.getStartTime();
        this.endTime = task.getEndTime();
        this.requiredTime = task.getRequiredTime();

        if(task.getAllDayState().equals(AllDayState.NO)) {
            this.allDayState = "NO";
        } else {
            this.allDayState = "YES";
        }

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

    }

}
