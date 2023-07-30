package com.example.taskqueue.category.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateCategoryDto {

    @Schema(description = "카텍고리 아이디", example = "1")
    @NotNull(message = "카테고리 아이디는 필수 값입니다")
    private Long categoryId;

    @Schema(description = "카테고리 이름", example = "study")
    @NotBlank(message = "카테고리 이름은 필수 값입니다")
    private String name;

    @Schema(description = "카테고리 색상", example = "A12345")
    @NotBlank(message = "카테고리 색상은 필수 값입니다")
    private String color;

    @Schema(description = "유저 아이디", example = "1")
    @NotNull(message = "유저 아이디는 필수 값입니다")
    private Long userId;
}
