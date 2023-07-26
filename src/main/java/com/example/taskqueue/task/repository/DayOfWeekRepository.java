package com.example.taskqueue.task.repository;

import com.example.taskqueue.task.entity.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Long> {
    /**
     * 요일 이름을 받아 요일을 반환한다.
     * @param name 요일 이름
     * @return 요일
     */
    DayOfWeek findDayOfWeekByName(String name);
}
