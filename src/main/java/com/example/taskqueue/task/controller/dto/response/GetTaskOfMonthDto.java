package com.example.taskqueue.task.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetTaskOfMonthDto {

    @Schema(description = "태스크 아이디 값", example = "1")
    private Long id;

    @Schema(description = "태스크 이름", example = "수학 과제")
    private String name;

    @Schema(description = "일자 정보")
    private List<LocalDate> dayList = new ArrayList<>();

    @Schema(description = "시작 시각", example = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime startTime;

    @Schema(description = "종료 시각", example = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endTime;

}
