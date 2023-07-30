package com.example.taskqueue.task.controller;

import com.example.taskqueue.category.service.CategoryService;
import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleTaskDto;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.task.controller.dto.request.CreateTaskDto;
import com.example.taskqueue.task.controller.dto.request.UpdateTaskDto;
import com.example.taskqueue.task.controller.dto.response.GetTaskDto;
import com.example.taskqueue.task.controller.dto.response.GetTaskListDto;
import com.example.taskqueue.task.entity.DayOfWeek;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.CompleteState;
import com.example.taskqueue.task.entity.state.ExpiredState;
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
import java.util.stream.Collectors;

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

    //우선순위 할당 : 생성마다 1씩 증가
    private int priority = 1;


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

    @Operation(summary = "유저 본인의 태스크 리스트 조회하기(우선순위 순)", description = "태스크 정보를 우선순위 순으로 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskListDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/tasks")
    public ResponseEntity<GetTaskListDto> getTaskList(
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        List<Task> findList = taskService.getTaskListByUserAndPriority(user);
        List<SimpleTaskDto> dtoList = findList.stream().map(SimpleTaskDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new GetTaskListDto(dtoList));
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
                .priority(priority)
                .allDayState(createTaskDto.getAllDayState())
                .calenderState(createTaskDto.getCalenderState())
                .repeatState(createTaskDto.getRepeatState())
                .completeState(CompleteState.NO)
                .expiredState(ExpiredState.NO)
                .build();

        //note 우선순위 증가
        priority++;

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

    @Operation(summary = "태스크 정보 수정(완료 여부 and 우선순위 제외)", description = "태스크 정보를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/tasks/{taskId}")
    public ResponseEntity<Void> updateTask(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid UpdateTaskDto updateTaskDto
    ) {
        //note 기본 정보 수정
        Task task = taskService.findById(taskId);
        task.updateName(updateTaskDto.getName());
        task.updateCategory(categoryService.findById(updateTaskDto.getCategoryId()));
        task.updateStartTime(updateTaskDto.getStartTime());
        task.updateEndTime(updateTaskDto.getEndTime());
        task.updateAllDayState(updateTaskDto.getAllDayState());
        task.updateRepeatState(updateTaskDto.getRepeatState());
        task.updateCalendarState(updateTaskDto.getCalenderState());

        //note 요일 정보 수정
        List<String> findList = updateTaskDto.getDayOfWeek();
        List<DayOfWeek> updateList =
                findList.stream().map(dayOfWeekRepository::findDayOfWeekByName).collect(Collectors.toList());

        taskService.changeDayOfWeek(task, updateList);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "태스크 완료 여부 TOGGLE", description = "태스크 완료 여부를 토글한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/tasks/{taskId}/complete/toggle")
    public ResponseEntity<Void> toggleCompleteState(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("taskId") Long taskId
    ) {
        Task findTask = taskService.findById(taskId);
        if(findTask.getCompleteState().equals(CompleteState.NO)) {
            findTask.updateCompleteState(CompleteState.YES);
        } else {
            findTask.updateCompleteState(CompleteState.NO);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "태스크 우선순위 SWAP", description = "태스크 2개의 우선순위를 교체한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/tasks/{taskId_1}/{taskId_2}/priority")
    public ResponseEntity<Void> swapTaskPriority(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("taskId_1") Long taskId_1,
            @PathVariable("taskId_2") Long taskId_2
    ) {
        Task taskA = taskService.findById(taskId_1);
        Task taskB = taskService.findById(taskId_2);

        int temp = taskA.getPriority();
        taskA.updatePriority(taskB.getPriority());
        taskB.updatePriority(temp);

        return ResponseEntity.noContent().build();
    }


}
