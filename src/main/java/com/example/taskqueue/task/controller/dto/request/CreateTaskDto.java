package com.example.taskqueue.task.controller.dto.request;

import com.example.taskqueue.task.entity.state.AllDayState;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.task.entity.state.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateTaskDto {

    @Schema(description = "태스크 이름", example = "수학 과제")
    private String name;

    @Schema(description = "태스크 시작 시간 - yyyy-MM-dd HH:mm", example = "2023-03-03 11:11")
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Schema(description = "태스크 종료 시간 - yyyy-MM-dd HH:mm", example = "2023-03-03 11:11")
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Schema(description = "태스크 우선순위", example = "1")
    @Positive(message = "우선순위 값은 양의 정수입니다.")
    @NotNull
    private int priority;

    @Schema(description = "종일 태스크 여부 - YES/NO", example = "YES")
    @NotBlank(message = "종일 태스크 여부는 필수 값입니다.")
    private AllDayState allDayState;

    @Schema(description = "루프 태스크 여부 - YES/NO", example = "YES")
    @NotBlank(message = "루프 태스크 여부는 필수 값입니다.")
    private RepeatState repeatState;

    @Schema(description = "루프 타입 - NOT/DAY/WEEK/MONTH", example = "NOT")
    @NotBlank(message = "루프 타입은 필수 값입니다.")
    private RepeatType repeatType;

    @Schema(description = "캘린더 표기 여부 - YES/NO", example = "YES")
    @NotBlank(message = "캘린더 표기 여부는 필수 값입니다.")
    private CalenderState calenderState;

}
