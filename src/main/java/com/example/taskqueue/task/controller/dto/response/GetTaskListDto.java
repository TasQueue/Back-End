package com.example.taskqueue.task.controller.dto.response;

import com.example.taskqueue.common.dto.SimpleTaskDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetTaskListDto {

    @ApiModelProperty(value = "유저 태스크 리스트")
    private List<SimpleTaskDto> taskList;

}
