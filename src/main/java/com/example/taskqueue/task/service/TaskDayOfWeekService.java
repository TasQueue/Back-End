package com.example.taskqueue.task.service;

import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.TaskDayOfWeek;
import com.example.taskqueue.task.repository.TaskDayOfWeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskDayOfWeekService {

    private final TaskDayOfWeekRepository taskDayOfWeekRepository;

    /**
     * 태스크의 요일 정보를 모두 삭제한다.
     * @param task 태스크 정보
     */
    public void deleteByTask(Task task) {
        taskDayOfWeekRepository.deleteAllByTask(task.getId());
    }

    /**
     * 태스크의 요일을 모두 반환한다.
     * @param taskId 태스크 아이디
     * @return 태스크의 요일 목록
     */
    public List<String> findByTask(Long taskId) {
        return taskDayOfWeekRepository.findDayOfWeekByTask(taskId);
    }
}
