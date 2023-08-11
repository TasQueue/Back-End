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
    private final DayOfWeekService dayOfWeekService;
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
     * 태스크를 생성한다.
     * @param task 저장할 태크스 정보
     * @param dayOfWeekList 저장할 태스크의 요일 정보
     * @return 저장한 태스크의 아이디 값
     */
    public Long saveTask(Task task, List<DayOfWeek> dayOfWeekList) {
        for (DayOfWeek day : dayOfWeekList) {
            TaskDayOfWeek taskDayOfWeek = new TaskDayOfWeek(task, day);
            taskDayOfWeekRepository.save(taskDayOfWeek);
        }
        return taskRepository.save(task).getId();
    }

    /**
     * 입력받은 요일 리트스틀 기준으로 태스크의 요일 정보를 수정한다.
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
     * 태스크를 삭제한다.
     * @param task 삭제할 태스크 정보
     */
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }


    /**
     * 특정 [일] 의 만료되지 않은 유저의 [일반 태스크] 를 우선순위 정렬하여 반환한다.
     * @param user 유저 정보
     * @param startOfDay 오늘의 00시 00분
     * @param endOfDay 내일의 00시 00분
     * @return 태스크 리스트
     */
    public List<Task> getTaskOfDay(
            User user,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    ) {
        return taskRepository.findTaskByUserAndPriority(
                user,
                ExpiredState.NO,
                RepeatState.NO,
                AllDayState.NO,
                startOfDay,
                endOfDay
        );
    }

    /**
     * 특정 [달] 의 만료되지 않은 유저의 [일반 태스크] 를 우선순위 정렬하여 반환한다.
     * @param user 유저 정보
     * @param startOfDay 특정 달 1일의 00시 00분
     * @param endOfDay 다음 달 1일의 00시 00분
     * @return 태스크 리스트
     */
    public List<Task> getTaskOfMonth(
            User user,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    ) {
        return taskRepository.findTaskByUserAndPriority(
                user,
                ExpiredState.NO,
                RepeatState.NO,
                AllDayState.NO,
                startOfDay,
                endOfDay
        );
    }


    /**
     * 유저의 루프 태스크를 찾아 모두 반환한다.
     * @param user 유저 정보
     * @return 루프 태스크 리스트
     */
    public List<Task> findRepeatTaskByUser(User user) {
        return taskRepository.findRepeatTaskByUser(user, ExpiredState.NO, RepeatState.YES);
    }

    /**
     * 유저의 일일 태스크를 찾아 모두 반환한다.
     * @param user 유저 정보
     * @return 일일 태스크 리스트
     */
    public List<Task> findAllDayTaskByUser(User user) {
        return taskRepository.findAllDayTaskByUser(user, ExpiredState.NO, AllDayState.YES);
    }

    /**
     * YES 상태의 완료여부를 가진 태스크의 비율에 따른 고양이의 상태를 측정하여 반환한다.
     * @param userId 유저 아이디
     */
    public int getStateOfCat(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        //note 자정 이후 첫 조회시 TotalTask 갱신 필요
        int countOfAvailableTask = taskRepository.countOfAvailableTask(userId, ExpiredState.NO);
        if(user.getTotalTask() != countOfAvailableTask) {
            user.updateTotalTask(countOfAvailableTask);
        }

        //note 나누기 0 연산 방지
        if(user.getTotalTask() == 0) {
            return 4;
        }

        int percentage =
                (100 * taskRepository.countOfCompleteTask(userId, CompleteState.YES, ExpiredState.NO)) / user.getTotalTask();

        if(percentage <= 100 && percentage >= 75) return 1;
        else if(percentage < 75 && percentage >= 50) return 2;
        else if(percentage < 50 && percentage >= 25) return 3;
        else return 4;
    }

    /**
     * 태스크 우선순위를 SWAP 한다. 루프 태스트(X) 일일 태스크(X)
     * 두 종류는 우선순위 SWAP 시도할 경우 400 ERROR 발생
     * @param taskId_1 태스크 1
     * @param taskId_2 태스크 2
     */
    public void swapTaskPriority(Long taskId_1, Long taskId_2) {
        Task taskA = taskRepository.findById(taskId_1).orElseThrow(TaskNotFoundException::new);
        Task taskB = taskRepository.findById(taskId_2).orElseThrow(TaskNotFoundException::new);

        if(taskA.getRepeatState().equals(RepeatState.YES) || taskB.getRepeatState().equals(RepeatState.YES)) {
            throw new LoopTaskPriorityBadInputException();
        }

        if(taskA.getAllDayState().equals(AllDayState.YES) || taskB.getAllDayState().equals(AllDayState.YES)) {
            throw new AllDayTaskPriorityBadInputException();
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
        if(findTask.getCompleteState().equals(CompleteState.NO)) {
            findTask.updateCompleteState(CompleteState.YES);
        } else {
            findTask.updateCompleteState(CompleteState.NO);
        }
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
        task.updateAllDayState(updateTaskDto.getAllDayState());
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

    /**
     * 매일 자정 : 예정일이 이틀이상 지난 태스크를 자동 만료처리한다.
     * 일일 태스크와 루프 태스크는 만료되지 않는다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredTask() {
        LocalDate presentDate = LocalDate.now();
        LocalDate expiryDate = presentDate.minusDays(2);

        //note DB 전체 만료태스크 전환
        taskRepository.updateExpiredTask(expiryDate, ExpiredState.YES, RepeatState.NO, AllDayState.NO);
    }

}
