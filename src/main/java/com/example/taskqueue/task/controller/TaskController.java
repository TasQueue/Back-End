package com.example.taskqueue.task.controller;

import com.example.taskqueue.category.service.CategoryService;
import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleTaskDto;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.task.controller.dto.request.CreateTaskDto;
import com.example.taskqueue.task.controller.dto.request.RequiredTaskMonthDto;
import com.example.taskqueue.task.controller.dto.request.UpdateTaskDto;
import com.example.taskqueue.task.controller.dto.response.GetTaskDto;
import com.example.taskqueue.task.controller.dto.response.GetTaskListDto;
import com.example.taskqueue.task.controller.dto.response.GetTaskOfMonthDto;
import com.example.taskqueue.task.controller.dto.response.GetTaskOfMonthListDto;
import com.example.taskqueue.task.entity.DayOfWeek;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.AllDayState;
import com.example.taskqueue.task.entity.state.CompleteState;
import com.example.taskqueue.task.entity.state.ExpiredState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.task.repository.DayOfWeekRepository;
import com.example.taskqueue.task.repository.TaskDayOfWeekRepository;
import com.example.taskqueue.task.service.TaskDayOfWeekService;
import com.example.taskqueue.task.service.TaskService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Task API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class TaskController {

    @Value("${domain.name}")
    private String host;

    private final TaskDayOfWeekService taskDayOfWeekService;
    private final DayOfWeekRepository dayOfWeekRepository;

    private final UserService userService;
    private final TaskService taskService;
    private final CategoryService categoryService;

    //우선순위 할당 : 생성마다 1씩 증가
    private int priority = 1;


    @ApiOperation(
            value = "태스크 정보 조회하기(단건)",
            notes = "태스크 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response =  GetTaskDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @GetMapping(value = "/tasks/{taskId}")
    public ResponseEntity<GetTaskDto> getTask(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("taskId") Long taskId
    ){
        Task task = taskService.findById(taskId);
        List<String> dayOfWeekList = taskDayOfWeekService.findByTask(taskId);
        return ResponseEntity.ok(new GetTaskDto(task, user, dayOfWeekList, task.getCategory()));
    }


    @ApiOperation(
            value = "오늘의 태스크 리스트 조회하기(우선순위 순)",
            notes = "태스크 정보를 우선순위 순으로 조회한다. <br> <br> " +
                    "일일 태스크는 삭제하지 않는 이상 항상 조회됩니다. <br> " +
                    "일일 태스크와 루프 태스크의 완료 여부는 고양이의 상태변화와는 무관합니다. <br> " +
                    "고양이의 상태변화는 일반태스크의 완료 비율 한정으로 계산합니다. <br> <br>" +
                    "일일 태스크 : 자동으로 생성 시 우선순위 최상이 됩니다. -> (-1) <br> " +
                    "루프 태스크 : 자동으로 생성 시 우선순위 그다음이 됩니다. -> (0) <br> "
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response =  GetTaskListDto.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @GetMapping(value = "/tasks")
    public ResponseEntity<GetTaskListDto> getTaskList(
            @ApiIgnore @CurrentUser User user
    ) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        LocalTime midnight = LocalTime.MIDNIGHT;

        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime tomorrowMidnight = LocalDateTime.of(tomorrow, midnight);

        List<Task> findList = taskService.getTaskListByUserAndPriority(user, todayMidnight, tomorrowMidnight);
        List<SimpleTaskDto> dtoList = findList.stream().map(SimpleTaskDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new GetTaskListDto(dtoList));
    }


    @ApiOperation(
            value = "태스크 생성하기",
            notes = "태스크를 생성한다. <br>" +
                    "일일 태스크 or 일반 태스크의 경우 dayOfWeek = [] (빈 리스트)로 넣어주시면 됩니다. <br>" +
                    "루프 태스크 (매일) 타입의 경우 dayOfWeek = [\"MON\", \"TUE\", .. \"SUN\"] 까지 모두 넣어주시면 됩니다. <br>" +
                    "startTime 과 endTime 은 yyyy-MM-dd HH:mm 타입을 반드시 지켜주시면 됩니다. <br> " +
                    "(..)State 관련 값은 반드시 \"NO\" 혹은 \"YES\" 값으로 넣어주시면 됩니다. <br> <br>" +
                    "일일 태스크의 경우에도 startTime 과 endTime 은 공백이여서는 안됩니다. <br> " +
                    "2023-01-01 00:00 와 같은 특정 값을 반드시 넣어주세요"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @PostMapping(value = "/tasks")
    public ResponseEntity<Void> createTask(
            @ApiIgnore @CurrentUser User user,
            @RequestBody @Valid CreateTaskDto createTaskDto
    ) {

        int current_priority = priority;

        //note 루프 태스크 & 일일 태스크 필터
        if(createTaskDto.getAllDayState().equals(AllDayState.YES) && createTaskDto.getRepeatState().equals(RepeatState.NO)) {
            current_priority = -1;
        } else if(createTaskDto.getAllDayState().equals(AllDayState.NO) && createTaskDto.getRepeatState().equals(RepeatState.YES)) {
            current_priority = 0;
        } else {
            priority++;
        }

        Task task = Task.builder()
                .name(createTaskDto.getName())
                .user(user)
                .category(categoryService.findById(createTaskDto.getCategoryId()))
                .startTime(createTaskDto.getStartTime())
                .endTime(createTaskDto.getEndTime())
                .priority(current_priority)
                .allDayState(createTaskDto.getAllDayState())
                .calenderState(createTaskDto.getCalenderState())
                .repeatState(createTaskDto.getRepeatState())
                .completeState(CompleteState.NO)
                .expiredState(ExpiredState.NO)
                .build();


        List<DayOfWeek> listOfDay =
                createTaskDto.getDayOfWeek().stream().map(dayOfWeekRepository::findDayOfWeekByName).collect(Collectors.toList());

        Long taskId = taskService.saveTask(task, listOfDay);
        userService.plusTotalTask(user);

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/tasks/{id}")
                .build(taskId);

        return ResponseEntity.created(uri).build();
    }



    @ApiOperation(
            value = "태스크 삭제하기",
            notes = "태스크를 삭제한다(복구 불가)."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("taskId") Long taskId
    ) {
        Task task = taskService.findById(taskId);
        taskDayOfWeekService.deleteByTask(task);
        taskService.deleteTask(task);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(
            value = "태스크 정보 수정(완료 여부 and 우선순위 제외)",
            notes = "태스크 정보를 수정한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @PutMapping(value = "/tasks/{taskId}")
    public ResponseEntity<Void> updateTask(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid UpdateTaskDto updateTaskDto
    ) {
        //note 기본 정보 수정
        Task task = taskService.findById(taskId);
        taskService.updateTaskBasicInfo(task, updateTaskDto);

        //note 요일 정보 수정
        List<String> findList = updateTaskDto.getDayOfWeek();
        List<DayOfWeek> updateList =
                findList.stream().map(dayOfWeekRepository::findDayOfWeekByName).collect(Collectors.toList());

        taskService.updateTaskDayOfWeekInfo(task, updateList);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(
            value = "태스크 완료 여부 TOGGLE",
            notes = "태스크 완료 여부를 토글한다.."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @PutMapping(value = "/tasks/{taskId}/complete/toggle")
    public ResponseEntity<Void> toggleCompleteState(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("taskId") Long taskId
    ) {
        taskService.toggleCompleteState(taskId);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(
            value = "태스크 우선순위 SWAP",
            notes = "태스크 2개의 우선순위를 교체한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @PutMapping(value = "/tasks/{taskId_1}/{taskId_2}/priority")
    public ResponseEntity<Void> swapTaskPriority(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("taskId_1") Long taskId_1,
            @PathVariable("taskId_2") Long taskId_2
    ) {
        taskService.swapTaskPriority(taskId_1, taskId_2);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(
            value = "월 별 태스크 조회",
            notes = "입력받은 월의 태스크를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response =  GetTaskOfMonthListDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping(value = "/users/{userId}/update/month")
    public ResponseEntity<GetTaskOfMonthListDto> getTaskListByMonth(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("userId") Long userId,
            @RequestBody @Valid RequiredTaskMonthDto requiredTaskMonthDto
    ) {
        User findUser = userService.findById(userId);

        LocalDate present = requiredTaskMonthDto.getLocalDate();
        LocalDate next = present.plusMonths(1);

        LocalTime localTime = LocalTime.of(0, 0, 0);
        LocalDateTime month = present.atTime(localTime);
        LocalDateTime nextMonth = next.atTime(localTime);



        //note 이 중에는 루프 태스크 and 일반 태스크가 섞여있다.
        List<Task> findList = taskService.getTaskOfMonth(findUser, month, nextMonth);
        List<GetTaskOfMonthDto> dtoList = new ArrayList<>();

        for (Task task : findList) {

            List<LocalDate> localDateList = new ArrayList<>();

            //note 일반 태스크
            if(task.getRepeatState().equals(RepeatState.NO)) {
                localDateList.add(task.getStartTime().toLocalDate());
                continue;
            }

            //note 루프 태스크
            if(task.getRepeatState().equals(RepeatState.YES))  {

                //note Java 기본형 DayOfWeek
                List<java.time.DayOfWeek> originList = new ArrayList<>();

                //note [MON, TUE, ... SUN]
                List<String> dayList = taskDayOfWeekService.findByTask(task.getId());
                for (String dayName : dayList) {
                    switch (dayName) {
                        case "MON":
                            originList.add(java.time.DayOfWeek.MONDAY);
                            break;
                        case "TUE":
                            originList.add(java.time.DayOfWeek.TUESDAY);
                            break;
                        case "WED":
                            originList.add(java.time.DayOfWeek.WEDNESDAY);
                            break;
                        case "THU":
                            originList.add(java.time.DayOfWeek.THURSDAY);
                            break;
                        case "FRI":
                            originList.add(java.time.DayOfWeek.FRIDAY);
                            break;
                        case "SAT":
                            originList.add(java.time.DayOfWeek.SATURDAY);
                            break;
                        default:
                            originList.add(java.time.DayOfWeek.SUNDAY);
                            break;
                    }
                }


                //note 해당 월의 루프태스크 일자 모두 찾아내기
                LocalDate startDate = task.getStartTime().toLocalDate();
                LocalDate endDate = startDate.plusMonths(1).withDayOfMonth(1);
                while (startDate.isBefore(endDate)) {

                    java.time.DayOfWeek dayOfWeek = startDate.getDayOfWeek();
                    if(originList.contains(dayOfWeek)) {
                        localDateList.add(startDate);
                    }

                    startDate = startDate.plusDays(1);

                }


            }

            LocalTime startTime = task.getStartTime().toLocalTime();
            LocalTime endTime = task.getEndTime().toLocalTime();
            dtoList.add(new GetTaskOfMonthDto(task.getId(), task.getName(), localDateList, startTime, endTime));

        }

        return ResponseEntity.ok(new GetTaskOfMonthListDto(dtoList));
    }







}
