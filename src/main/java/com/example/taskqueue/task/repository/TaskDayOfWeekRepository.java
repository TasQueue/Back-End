package com.example.taskqueue.task.repository;

import com.example.taskqueue.task.entity.TaskDayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskDayOfWeekRepository extends JpaRepository<TaskDayOfWeek, Long> {

    /**
     * 태스크 아이디 값을 받아 해당 요일 리스트를 반환한다.
     * @param taskId 태스크 아이디 값
     * @return 요일 리스트 정보 ex) [MON, TUE, WED, FRI]
     */
    @Query("select tdw.dayOfWeek.name from TaskDayOfWeek tdw where tdw.task.id = :taskId")
    List<String> findDayOfWeekByTask(@Param("taskId") Long taskId);
}
