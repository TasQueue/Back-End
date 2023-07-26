package com.example.taskqueue.task.controller.dto.request;

import com.example.taskqueue.task.entity.state.AllDayState;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.RepeatState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateTaskDto {

    @Schema(description = "태스크 이름", example = "수학 과제")
    @NotBlank(message = "태스크 이름은 필수 값입니다.")
    private String name;

    @Schema(description = "태스크 시작 시간 - yyyy-MM-dd HH:mm", example = "2023-03-03 11:11")
    @NotNull(message = "태스크 시작 시간은 필수 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Schema(description = "태스크 종료 시간 - yyyy-MM-dd HH:mm", example = "2023-03-03 11:11")
    @NotNull(message = "태스크 종료 시간은 필수 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Schema(description = "태스크 요일 리스트", example = "[MON, TUE, WED]")
    @NotNull(message = "태스크 요일은 필수 값입니다.")
    private List<String> DayOfWeek;

    @Schema(description = "태스크 우선순위", example = "1")
    @NotNull(message = "우선순위 정보는 필수 값입니다.")
    @Positive(message = "우선순위 값은 양의 정수입니다.")
    private int priority;

    @Schema(description = "종일 태스크 여부 - YES/NO", example = "YES")
    @NotBlank(message = "종일 태스크 여부는 필수 값입니다.")
    private AllDayState allDayState;

    @Schema(description = "루프 태스크 여부 - YES/NO", example = "YES")
    @NotBlank(message = "루프 태스크 여부는 필수 값입니다.")
    private RepeatState repeatState;

    @Schema(description = "캘린더 표기 여부 - YES/NO", example = "YES")
    @NotBlank(message = "캘린더 표기 여부는 필수 값입니다.")
    private CalenderState calenderState;

}
