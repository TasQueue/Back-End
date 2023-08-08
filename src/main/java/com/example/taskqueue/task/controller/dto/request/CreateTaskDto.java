package com.example.taskqueue.task.controller.dto.request;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.task.entity.state.AllDayState;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {

    @ApiModelProperty(value = "태스크 이름", example = "수학 과제")
    @NotBlank(message = "태스크 이름은 필수 값입니다.")
    private String name;

    @ApiModelProperty(value = "태스크 카테고리 아이디 값", example = "1")
    @NotNull(message = "카테고리 아이디는 필수 값입니다.")
    private Long categoryId;

    @ApiModelProperty(value = "태스크 시작 시간 - yyyy-MM-dd HH:mm", example = "2023-03-03 11:11")
    @NotNull(message = "태스크 시작 시간은 필수 값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "태스크 종료 시간 - yyyy-MM-dd HH:mm", example = "2023-03-03 11:11")
    @NotNull(message = "태스크 종료 시간은 필수 값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "태스크 요일 리스트", example = "[\"MON\", \"TUE\", \"WED\"]")
    @NotNull(message = "태스크 요일은 필수 값입니다.")
    private List<String> dayOfWeek;

    @ApiModelProperty(value = "종일 태스크 여부 - YES/NO", example = "YES")
    @NotNull(message = "종일 태스크 여부는 필수 값입니다.")
    private AllDayState allDayState;

    @ApiModelProperty(value = "루프 태스크 여부 - YES/NO", example = "YES")
    @NotNull(message = "루프 태스크 여부는 필수 값입니다.")
    private RepeatState repeatState;

    @ApiModelProperty(value = "캘린더 표기 여부 - YES/NO", example = "YES")
    @NotNull(message = "캘린더 표기 여부는 필수 값입니다.")
    private CalenderState calenderState;

}
