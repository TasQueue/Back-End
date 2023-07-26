package com.example.taskqueue.common.dto;

import com.example.taskqueue.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleCategoryDto {

    @Schema(description = "카테고리 아이디 값", example = "1")
    private Long id;

    @Schema(description = "카테고리 이름", example = "학교 수업")
    private String name;

    @Schema(description = "카테고리 색상", example = "ABC123")
    private String color;

    public SimpleCategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.color = category.getColor();
    }

}
