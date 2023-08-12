package com.example.taskqueue.task.service;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.category.repository.CategoryRepository;
import com.example.taskqueue.exception.badinput.AllDayTaskPriorityBadInputException;
import com.example.taskqueue.exception.badinput.LoopTaskPriorityBadInputException;
import com.example.taskqueue.exception.notfound.CategoryNotFoundException;
import com.example.taskqueue.exception.notfound.TaskNotFoundException;
import com.example.taskqueue.exception.notfound.UserNotFoundException;
import com.example.taskqueue.task.controller.dto.request.UpdateTaskDto;
import com.example.taskqueue.task.entity.DayOfWeek;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.TaskDayOfWeek;
import com.example.taskqueue.task.entity.state.*;
import com.example.taskqueue.task.repository.TaskDayOfWeekRepository;
import com.example.taskqueue.task.repository.TaskRepository;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskDayOfWeekRepository taskDayOfWeekRepository;

    /**
     * 입력된 아이디 값으로 태스크를 찾아 반환한다.
     * @param id 아이디 정보
     * @return 태스크
     */
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    /**
     * 입력받은 태스크를 저장한다.
     * 태스크를 저장하면서 태스크의 요일정보도 함께 저장한다.
     * 유저의 TotalTask 증가
     *
     * @param task 태크스 정보
     * @param dayOfWeekList 태스크의 요일 정보
     * @return 저장한 태스크의 아이디 값
     */
    public Long saveTask(Task task, List<DayOfWeek> dayOfWeekList) {
        for (DayOfWeek day : dayOfWeekList) {
            TaskDayOfWeek taskDayOfWeek = new TaskDayOfWeek(task, day);
            taskDayOfWeekRepository.save(taskDayOfWeek);
        }

        User user = task.getUser();
        return taskRepository.save(task).getId();
    }

    /**
     * 태스크를 삭제한다.
     * 유저의 TotalTask 감소
     * @param task 삭제할 태스크 정보
     */
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    /**
     * 입력받은 요일 리트스틀 기준으로 태스크의 요일 정보를 수정한다.
     * 기존의 요일정보는 모두 삭제한다.
     *
     * @param task 태스크 정보
     * @param dayOfWeekList 새로 갱신할 요일 리스트 정보
     */
    public void updateTaskDayOfWeekInfo(Task task, List<DayOfWeek> dayOfWeekList) {
        taskDayOfWeekRepository.deleteAllByTask(task.getId());
        for (DayOfWeek day : dayOfWeekList) {
            TaskDayOfWeek taskDayOfWeek = new TaskDayOfWeek(task, day);
            taskDayOfWeekRepository.save(taskDayOfWeek);
        }
    }

    /**
     * 특정 기간 유저의 [일반 태스크] 를 우선순위 정렬하여 반환한다.
     * @param user 유저 정보
     * @param startOfDay 기간의 시작
     * @param endOfDay 기간의 끝
     * @return 태스크 리스트
     */
    public List<Task> getTaskList(
            User user,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    ) {
        return taskRepository.findTaskByUserAndPriority(
                user,
                RepeatState.NO,
                startOfDay,
                endOfDay
        );
    }

    /**
     * 특정 기간 유저의 [시간표 ON 일반 태스크] 를 우선순위 정렬하여 반환한다.
     * @param user 유저 정보
     * @param startOfDay 기간의 시작
     * @param endOfDay 기간의 끝
     * @return 태스크 리스트
     */
    public List<Task> getTaskOnScheduleList(
            User user,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    ) {
        return taskRepository.findTaskOnScheduleByUserAndPriority(
                user,
                RepeatState.NO,
                startOfDay,
                endOfDay
        );
    }

    /**
     * 특정 [달] 의 만료되지 않은 유저의 [캘린더 ON 일반 태스크] 를 우선순위 정렬하여 반환한다.
     * @param user 유저 정보
     * @param startOfDay 특정 달 1일의 00시 00분
     * @param endOfDay 다음 달 1일의 00시 00분
     * @return 태스크 리스트
     */
    public List<Task> getTaskOnCalenderOfMonth(
            User user,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    ) {
        return taskRepository.findTaskOnCalenderByUserAndPriority(
                user,
                RepeatState.NO,
                CalenderState.YES,
                startOfDay,
                endOfDay
        );
    }

    /**
     * 유저의 [특정 시점 이후] 의 [모든] 루프 태스크를 찾아 모두 반환한다.
     * @param user 유저 정보
     * @return 루프 태스크 리스트
     */
    public List<Task> findRepeatTaskByUser(User user, LocalDateTime startTime) {
        return taskRepository.findRepeatTaskByUser(user, RepeatState.YES, startTime);
    }

    /**
     * 유저의 [캘린더 ON 상태] 루프 태스크를 찾아 모두 반환한다.
     * @param user 유저 정보
     * @return 루프 태스크 리스트
     */
    public List<Task> findRepeatTaskOnCalenderByUser(User user) {
        return taskRepository.findRepeatTaskOnCalenderByUser(user, RepeatState.YES, CalenderState.YES);
    }

    /**
     * 유저 고양이 상태를 반환한다.
     * @param userId 유저 아이디
     */
    public int getStateOfCat(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getRunStreak();
    }

    /**
     * 태스크 우선순위를 SWAP 한다. 루프 태스트(X) 시도할 경우 400 ERROR 발생
     * @param taskId_1 태스크 1
     * @param taskId_2 태스크 2
     */
    public void swapTaskPriority(Long taskId_1, Long taskId_2) {
        Task taskA = taskRepository.findById(taskId_1).orElseThrow(TaskNotFoundException::new);
        Task taskB = taskRepository.findById(taskId_2).orElseThrow(TaskNotFoundException::new);

        if(taskA.getRepeatState().equals(RepeatState.YES) || taskB.getRepeatState().equals(RepeatState.YES)) {
            throw new LoopTaskPriorityBadInputException();
        }

        int temp = taskA.getPriority();
        taskA.updatePriority(taskB.getPriority());
        taskB.updatePriority(temp);
    }

    /**
     * 태스크의 CompleteState 를 TOGGLE 한다.
     * @param taskId 태스크 아이디 값
     */
    public void toggleCompleteState(Long taskId) {
        Task findTask = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        User user = findTask.getUser();
        if(findTask.getCompleteState().equals(CompleteState.NO))
            findTask.updateCompleteState(CompleteState.YES);
        else
            findTask.updateCompleteState(CompleteState.NO);
    }

    /**
     * 태스크 정보를 변경한다.
     * @param updateTaskDto 태스크 변경 정보
     */
    public void updateTaskBasicInfo(Task task, UpdateTaskDto updateTaskDto) {
        Category category = categoryRepository.findById(updateTaskDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        task.updateName(updateTaskDto.getName());
        task.updateCategory(category);
        task.updateStartTime(updateTaskDto.getStartTime());
        task.updateEndTime(updateTaskDto.getEndTime());
        task.updateRepeatState(updateTaskDto.getRepeatState());
        task.updateCalendarState(updateTaskDto.getCalenderState());
    }

    /**
     * 루프태스크의 경우 해당 날짜의 태스크에 해당되는지 여부를 반환한다.
     * @param task 루프 태스크
     * @return 해당 여부
     */
    public boolean isTaskOfThisDay(String day, Task task) {
        List<String> dayOfWeekByTask = taskDayOfWeekRepository.findDayOfWeekByTask(task.getId());
        return dayOfWeekByTask.contains(day);
    }

}
