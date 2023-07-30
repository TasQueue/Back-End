package com.example.taskqueue.category.controller;

import com.example.taskqueue.category.controller.dto.CategoryListDto;
import com.example.taskqueue.category.controller.dto.CategoryUpdateDto;
import com.example.taskqueue.category.controller.dto.CreateCategoryDto;
import com.example.taskqueue.category.entity.Category;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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

    // 카테고리 조회
    @ApiResponse()
    @ResponseStatus()
    @GetMapping("/api/categories")
    public ResponseEntity<CategoryListDto> getCategoryList(
            @Parameter(hidden = true) @CurrentUser User user){
        List<Long> category = categoryService.findAllById(user.getId());
        List<SimpleCategoryDto> dtoList = new ArrayList<>();

        for (Long categoryId : category){
            SimpleCategoryDto categoryDto = new SimpleCategoryDto(categoryService.findById(categoryId));
            dtoList.add(categoryDto);
        }
        return ResponseEntity.ok(new CategoryListDto(dtoList));
    }

    // 카테고리 생성
    @ApiResponse()
    @ResponseStatus()
    @PostMapping("/api/categories")
    public ResponseEntity<Long> saveCategory(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody @Valid CreateCategoryDto createCategoryDto
    ){
        Category category = Category.builder()
                .user(user)
                .name(createCategoryDto.getName())
                .color(createCategoryDto.getColor())
                .build();

        Long categoryId = categoryService.save(category);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/categories/{id}")
                .build(categoryId);

        return ResponseEntity.created(uri).build();
    }

    // 카테고리 수정
    @ApiResponse
    @ResponseStatus
    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<Void> updateCategoryInfo(
            @Parameter(hidden = true)
            @PathVariable("categoryId") Long categoryId,
            @ModelAttribute CategoryUpdateDto categoryUpdateDto
    ) {
        Category category = categoryService.findById(categoryId);

        category.updateName(categoryUpdateDto.getName());
        category.updateColor(categoryUpdateDto.getColor());

        return ResponseEntity.noContent().build();
    }

    // 카테고리 삭제
    @DeleteMapping("/api/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("categoryId") Long categoryId
    ) {
        Category category = categoryService.findById(categoryId);

        categoryService.delete(category);

        return ResponseEntity.noContent().build();
    }

}
