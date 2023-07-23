package com.example.taskqueue.task.service;

import com.example.taskqueue.exception.notfound.TaskNotFoundException;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.CalenderState;
import com.example.taskqueue.task.entity.state.CompleteState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.task.repository.TaskRepository;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
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
     * 태스크를 루프 태스크로 전환한다.
     * @param task 전환할 태스크 정보
     */
    public void taskRepeatON(Task task) {
        task.updateRepeatState(RepeatState.YES);
    }

    /**
     * 루프 태스크를 일반 태스크로 전환한다.
     * @param task 전환할 태스크 정보
     */
    public void taskRepeatOFF(Task task) {
        task.updateRepeatState(RepeatState.NO);
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

    //TODO 태스크 우선순위 수정

    //TODO 태스크 시작시간 및 종료시간 수정

}
