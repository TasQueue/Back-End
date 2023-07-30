package com.example.taskqueue.common.dto;

import com.example.taskqueue.task.entity.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class SimpleTaskDto {

    @Schema(description = "태스크 아이디 값", example = "1")
    private Long id;

    @Schema(description = "태스크 이름", example = "수학 과제")
    private String name;

    @Schema(description = "태스크 우선순위", example = "1")
    private int priority;

    @Schema(description = "태스크 시작 시간", example = "2023-08-08 13:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @Schema(description = "태스크 종료 시간", example = "2023-08-08 14:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    public SimpleTaskDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.priority = task.getPriority();
        this.startTime = task.getStartTime();
        this.endTime = task.getEndTime();
    }

}
