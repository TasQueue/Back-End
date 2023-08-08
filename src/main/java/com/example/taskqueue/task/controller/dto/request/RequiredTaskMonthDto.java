package com.example.taskqueue.task.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequiredTaskMonthDto {

    @ApiModelProperty(value = "요구하는 연/월", example = "2023-08")
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate localDate;

}
