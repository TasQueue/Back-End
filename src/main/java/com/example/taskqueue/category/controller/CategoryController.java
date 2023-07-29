package com.example.taskqueue.category.controller;

import com.example.taskqueue.category.controller.dto.CategoryListDto;
import com.example.taskqueue.category.service.CategoryService;
import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleCategoryDto;
import com.example.taskqueue.user.entity.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Category", description = "Category 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class CategoryController {

    @Value("${domain.name}")
    private String host;

    private CategoryService categoryService;

//    @ApiResponse()
//    @ResponseStatus()
//    @GetMapping("/api/categories")
//    public ResponseEntity<CategoryListDto> getCategoryList(
//            @Parameter(hidden = true) @CurrentUser User user){
//        List<String> category = categoryService.findAll();
//        List<SimpleCategoryDto> dtoList = new ArrayList<>();
//
//        for ()
//        return ResponseEntity.ok(new CategoryListDto(category));
//    }

}
