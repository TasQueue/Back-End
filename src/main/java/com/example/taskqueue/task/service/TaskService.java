package com.example.taskqueue.task.service;

import com.example.taskqueue.exception.notfound.TaskNotFoundException;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.repository.TaskRepository;
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


    /**
     * 입력된 아이디 값으로 태스크를 찾아 반환한다.
     * @param id 아이디 정보
     * @return 태스크
     */
    public Task findById(Long id) {
        return null;
    }

    /**
     *
     * @param task 저장할 태크스
     * @return
     */
    public Long saveTask(Task task) {
        return taskRepository.save(task).getId();
    }

    //TODO 태스크 우선순위 수정

    //TODO 태스크 시작시간 및 종료시간 수정

    //TODO 태스크 생성



    //TODO 태스크 삭제

    //TODO 태스크 루프 ON / OFF

    //TODO 태스크 완료 ON / OFF

    //TODO 태스크

}
