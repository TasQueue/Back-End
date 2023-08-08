package com.example.taskqueue.task.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequiredTaskDayDto {

    @ApiModelProperty(value = "요구하는 연/월", example = "2023-08-08")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "날짜는 필수 값입니다.")
    private LocalDate localDate;

}
