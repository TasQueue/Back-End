package com.example.taskqueue.task.repository;

import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.TaskDayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDayOfWeekRepository extends JpaRepository<TaskDayOfWeek, Long> {
    /**
     * 입력받은 태스크로 요일 매핑 정보를 반환한다.
     * @param task 태스크 정보
     * @return 중간 매핑 정보
     */
    TaskDayOfWeek findTaskDayOfWeekByTask(Task task);
}
