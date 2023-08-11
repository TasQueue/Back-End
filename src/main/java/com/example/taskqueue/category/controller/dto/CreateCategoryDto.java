package com.example.taskqueue.category.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {

    @ApiModelProperty(value = "카테고리 이름", example = "study")
    @NotBlank(message = "카테고리 이름은 필수 값입니다")
    private String name;

    @ApiModelProperty(value = "카테고리 색상", example = "A12345")
    @NotBlank(message = "카테고리 색상은 필수 값입니다")
    private String color;

}
