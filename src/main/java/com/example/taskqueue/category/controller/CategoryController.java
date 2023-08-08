package com.example.taskqueue.category.controller;

import com.example.taskqueue.category.controller.dto.CategoryListDto;
import com.example.taskqueue.category.controller.dto.CategoryUpdateDto;
import com.example.taskqueue.category.controller.dto.CreateCategoryDto;
import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.category.service.CategoryService;
import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleCategoryDto;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Api(tags = "Category API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Value("${domain.name}")
    private String host;

    @ApiOperation(
            value = "유저 본인의 카테고리 목록 조회하기",
            notes = "자신의 카테고리 목록을 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = CategoryListDto.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @GetMapping("/categories")
    public ResponseEntity<CategoryListDto> getCategoryList(
            @ApiIgnore @CurrentUser User user
    ){
        List<Category> findList = categoryService.findByUser(user);
        List<SimpleCategoryDto> dtoList = findList.stream().map(SimpleCategoryDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new CategoryListDto(dtoList));
    }


    @ApiOperation(
            value = "카테고리 생성하기",
            notes = "카테고리를 생성 및 저장한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = CategoryListDto.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(
            @ApiIgnore @CurrentUser User user,
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
                .path("/categories/{id}")
                .build(categoryId);

        return ResponseEntity.created(uri).build();
    }


    @ApiOperation(
            value = "카테고리 정보 수정하기",
            notes = "카테고리 정보를 수정한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Void> updateCategoryInfo(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("categoryId") Long categoryId,
            @RequestBody @Valid CategoryUpdateDto categoryUpdateDto
    ) {
        Category category = categoryService.findById(categoryId);
        categoryService.updateCategory(category, categoryUpdateDto);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(
            value = "카테고리 삭제하기",
            notes = "카테고리 정보를 삭제한다(복구 불가)."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("categoryId") Long categoryId
    ) {
        Category category = categoryService.findById(categoryId);
        categoryService.delete(category);
        return ResponseEntity.noContent().build();
    }

}
