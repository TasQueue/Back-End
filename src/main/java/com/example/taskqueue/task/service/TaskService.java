package com.example.taskqueue.task.service;

import com.example.taskqueue.exception.notfound.TaskNotFoundException;
import com.example.taskqueue.exception.notfound.UserNotFoundException;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.CompleteState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.task.entity.state.RepeatType;
import com.example.taskqueue.task.repository.TaskRepository;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

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
     * @return 저장한 태스크의 아이디 값
     */
    public Long saveTask(Task task) {
        return taskRepository.save(task).getId();
    }

    /**
     * 태스크를 삭제한다.
     * @param task 삭제할 태스크 정보
     */
    public void deleteTask(Task task) { taskRepository.delete(task);}


    /**
     * 입력받은 태스크를 루프 태스크로 전환하고, 루프 타입을 설정한다.
     * @param task 태스크 정보
     * @param repeatType 루프 타입 정보
     */
    public void taskRepeatON(Task task, RepeatType repeatType) {
        task.updateRepeatType(repeatType);
        task.updateRepeatState(RepeatState.YES);
    }

    /**
     * 루프 태스크를 일반 태스크로 전환한다.
     * @param task 전환할 태스크 정보
     */
    public void taskRepeatOFF(Task task) {
        task.updateRepeatState(RepeatState.NO);
        task.updateRepeatType(RepeatType.NOT);
    }

    /**
     * 태스크를 완료 상태로 전환한다.
     * @param task 전환할 태스크 정보
     */
    public void taskCompleteON(Task task) {
        task.updateCompleteState(CompleteState.YES);
    }

    /**
     * 태스크를 미완료 상태로 전환한다.
     * @param task 전환할 태스크 정보
     */
    public void taskCompleteOFF(Task task) {
        task.updateCompleteState(CompleteState.NO);
    }

    /**
     * 태스크를 캘린더 표기 상태로 전환한다.
     * @param task 전환할 태스크 정보
     */
    public void taskCalenderON(Task task) {
        task.updateCalendarState(CalenderState.YES);
    }

    /**
     * 태스크를 캘린더 미표기 상태로 전환한다.
     * @param task 전환할 태스크 정보
     */
    public void taskCalenderOFF(Task task) {
        task.updateCalendarState(CalenderState.NO);
    }

    /**
     * YES 상태의 완료여부를 가진 태스크의 비율에 따른 고양이의 상태를 측정하여 반환한다.
     * @param userId 유저 아이디
     */
    public int getStateOfCat(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        int percentage = 100 * taskRepository.countOfCompleteTask(userId, CompleteState.YES) / user.getTotalTask();

        if(percentage <= 100 && percentage >= 75) return 1;
        else if(percentage < 75 && percentage >= 50) return 2;
        else if(percentage < 50 && percentage >= 25) return 3;
        else return 4;
    }

    /**
     * 매일 자정 예정일이 이틀이상 지난 만료 태스크를 삭제한다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredTask() {
        LocalDate presentDate = LocalDate.now();
        LocalDate expiryDate = presentDate.minusDays(2);
        taskRepository.deleteExpiredTask(expiryDate);
    }

}
