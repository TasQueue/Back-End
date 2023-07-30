package com.example.taskqueue.category.controller.dto;

import com.example.taskqueue.common.dto.SimpleCategoryDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryListDto {

    @Schema(description = "category 리스트")
    @JsonProperty("categoryList")
    private List<SimpleCategoryDto> simpleCategoryDtos = new ArrayList<>();
}
