package com.example.taskqueue.category.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class CategoryUpdateDto {

    @Schema(description = "변경할 카테고리 이름 ", example = "study")
    @NotBlank(message = "카테고리 명은 필수값 입니다.")
    private String name;

    @Schema(description = "변경할 카테고리 색상", example = "ABC123")
    private String color;
}
