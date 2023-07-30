package com.example.taskqueue.task.controller.dto.response;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.common.dto.SimpleCategoryDto;
import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.AllDayState;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetTaskDto {

    @Schema(description = "태스크 아이디 값", example = "1")
    private Long id;

    @Schema(description = "태스크 이름", example = "수학 과제하기")
    private String name;

    @Schema(description = "태스크 요일 정보", example = "[MON, TUE, WED, THU]")
    private List<String> dayOfWeek;

    @Schema(description = "태스크 소유 유저 정보")
    @JsonProperty("user")
    private SimpleUserDto simpleUserDto;

    @Schema(description = "태스크 카테고리 정보")
    @JsonProperty("category")
    private SimpleCategoryDto simpleCategoryDto;

    @Schema(description = "태스크 시작시간 정보")
    private LocalDateTime startTime;

    @Schema(description = "태스크 종료시간 정보")
    private LocalDateTime endTime;

    @Schema(description = "종일 태스크 여부", example = "YES/NO")
    private AllDayState allDayState;

    @Schema(description = "루프 태스크 여부", example = "YES/NO")
    private RepeatState repeatState;

    @Schema(description = "달력 표기 여부", example = "YES/NO")
    private CalenderState calenderState;

    public GetTaskDto(Task task, User user, List<String> dayOfWeek, Category category) {
        this.id = task.getId();
        this.name = task.getName();
        this.dayOfWeek = dayOfWeek;
        this.simpleUserDto = new SimpleUserDto(user);
        this.simpleCategoryDto = new SimpleCategoryDto(category);
        this.startTime = task.getStartTime();
        this.endTime = task.getEndTime();
        this.allDayState = task.getAllDayState();
        this.repeatState = task.getRepeatState();
        this.calenderState = task.getCalenderState();
    }

}
