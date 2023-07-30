package com.example.taskqueue.task.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetTaskOfMonthListDto {

    @Schema(description = "태스크 정보")
    List<GetTaskOfMonthDto> taskList = new ArrayList<>();

}
