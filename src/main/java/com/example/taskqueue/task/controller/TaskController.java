package com.example.taskqueue.task.controller;

import com.example.taskqueue.task.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Task", description = "Task 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;




}
