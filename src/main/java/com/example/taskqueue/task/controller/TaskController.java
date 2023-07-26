package com.example.taskqueue.task.controller;

import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.task.controller.dto.request.CreateTaskDto;
import com.example.taskqueue.task.service.TaskService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Task", description = "Task 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    //TODO 태스크 조회하기

    @Operation(summary = "태스크 생성하기", description = "태스크를 생성한다..", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/tasks/{categoryId}")
    public ResponseEntity<Void> createTask(
            @Parameter(hidden = true) User user,
            @PathVariable("categoryId") Long categoryId,
            @ModelAttribute CreateTaskDto createTaskDto
    ) {
        return null;
    }


    //TODO 태스크 삭제하기

    //TODO 태스크 수정하기

    //TODO 월별 태스크 조회하기

}
