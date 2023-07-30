package com.example.taskqueue.task.controller;

import com.example.taskqueue.category.service.CategoryService;
import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.task.controller.dto.request.CreateTaskDto;
import com.example.taskqueue.task.controller.dto.response.GetTaskDto;
import com.example.taskqueue.task.entity.DayOfWeek;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.repository.DayOfWeekRepository;
import com.example.taskqueue.task.repository.TaskDayOfWeekRepository;
import com.example.taskqueue.task.service.TaskService;
import com.example.taskqueue.user.entity.User;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Tag(name = "Task", description = "Task 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class TaskController {

    @Value("${domain.name}")
    private String host;

    private final TaskDayOfWeekRepository taskDayOfWeekRepository;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final TaskService taskService;
    private final CategoryService categoryService;

    @Operation(summary = "태스크 정보 조회하기(단건)", description = "태스크 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/tasks/{taskId}")
    public ResponseEntity<GetTaskDto> getTask(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("taskId") Long taskId
    ){
        Task task = taskService.findById(taskId);
        List<String> dayOfWeekList = taskDayOfWeekRepository.findDayOfWeekByTask(taskId);
        return ResponseEntity.ok(new GetTaskDto(task, user, dayOfWeekList, task.getCategory()));
    }


    @Operation(summary = "태스크 생성하기", description = "태스크를 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/tasks")
    public ResponseEntity<Void> createTask(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody @Valid CreateTaskDto createTaskDto
    ) {
        Task task = Task.builder()
                .name(createTaskDto.getName())
                .user(user)
                .category(categoryService.findById(createTaskDto.getCategoryId()))
                .startTime(createTaskDto.getStartTime())
                .endTime(createTaskDto.getEndTime())
                .priority(createTaskDto.getPriority())
                .allDayState(createTaskDto.getAllDayState())
                .calenderState(createTaskDto.getCalenderState())
                .repeatState(createTaskDto.getRepeatState())
                .build();


        List<DayOfWeek> listOfDay = new ArrayList<>();
        for (String dayName : createTaskDto.getDayOfWeek()) {
            DayOfWeek newDay = dayOfWeekRepository.findDayOfWeekByName(dayName);
            listOfDay.add(newDay);
        }

        Long taskId = taskService.saveTask(task, listOfDay);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/tasks/{id}")
                .build(taskId);

        return ResponseEntity.created(uri).build();
    }


    @Operation(summary = "태스크 삭제하기", description = "태스크를 삭제한다(복구 불가)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("taskId") Long taskId
    ) {
        Task task = taskService.findById(taskId);
        taskService.deleteTask(task);
        return ResponseEntity.noContent().build();
    }



    //TODO 태스크 수정하기

    //TODO 월별 태스크 조회하기

}
