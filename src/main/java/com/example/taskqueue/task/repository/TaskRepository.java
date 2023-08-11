package com.example.taskqueue.task.repository;

import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.*;
import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    /**
     * 유저의 [특정 시점 이후] 의 [모든] 루프태스크를 모두 반환한다.
     * @param user 유저 정보
     * @param repeatState RepeatState.YES
     * @param startTime 특정 시점
     * @return 루프 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.repeatState = :repeatState " +
            "and t.startTime < :startTime")
    List<Task> findRepeatTaskByUser(
            @Param("user") User user,
            @Param("repeatState") RepeatState repeatState,
            @Param("startTime") LocalDateTime startTime
    );

    /**
     * 유저의 [캘린더 ON 상태] 루프태스크를 모두 반환한다.
     * @param user 유저 정보
     * @param repeatState RepeatState.YES
     * @param calenderState CalenderState.YES
     * @return 루프 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.repeatState = :repeatState " +
            "and t.calenderState = :calenderState")
    List<Task> findRepeatTaskOnCalenderByUser(
            @Param("user") User user,
            @Param("repeatState") RepeatState repeatState,
            @Param("calenderState") CalenderState calenderState
    );

    /**
     * 특정 기간의 유저 [기본] 태스크를 우선순위 순으로 정렬하여 반환한다.
     * @param user 유저 정보
     * @param startOfDay 기간의 시작
     * @param endOfDay 기간의 끝
     * @return 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.repeatState = :repeatState " +
            "and t.startTime >= :startOfDay " +
            "and t.endTime < :endOfDay " +
            "order by t.priority")
    List<Task> findTaskByUserAndPriority(
            @Param("user") User user,
            @Param("repeatState") RepeatState repeatState,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * 특정 기간의 유저 [캘린더 ON 기본] 태스크를 우선순위 순으로 정렬하여 반환한다.
     * @param user 유저 정보
     * @param startOfDay 기간의 시작
     * @param endOfDay 기간의 끝
     * @return 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.repeatState = :repeatState " +
            "and t.calenderState = :calenderState "+
            "and t.startTime >= :startOfDay " +
            "and t.endTime < :endOfDay " +
            "order by t.priority")
    List<Task> findTaskOnCalenderByUserAndPriority(
            @Param("user") User user,
            @Param("repeatState") RepeatState repeatState,
            @Param("calenderState") CalenderState calenderState,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );


}
