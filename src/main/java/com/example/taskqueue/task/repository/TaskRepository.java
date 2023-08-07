package com.example.taskqueue.task.repository;

import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.CompleteState;
import com.example.taskqueue.task.entity.state.ExpiredState;
import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    /**
     * 만료되지 않은 유저의 태스크를 우선순위 순으로 정렬하여 반환한다.
     * @param user 유저 정보
     * @param expiredState ExpiredState.NO
     * @return 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.expiredState = :expiredState " +
            "order by t.priority")
    List<Task> findTaskByUserAndPriority(@Param("user") User user, @Param("expiredState") ExpiredState expiredState);

    /**
     * 특정 유저의 만료되지 않은 완료 태스크의 갯수를 반환한다.
     * @param userId 유저 아이디 값
     * @param completeState CompleteState.YES
     * @param expiredState ExpiredState.NO
     * @return
     */
    @Query("select count(t) from Task t where t.user.id = :userId " +
            "and t.completeState = :completeState " +
            "and t.expiredState = :expiredState")
    int countOfCompleteTask(@Param("userId") Long userId,
                            @Param("completeState") CompleteState completeState,
                            @Param("expiredState") ExpiredState expiredState);

    /**
     * 모든 시스템 유저의 만료일이 지난 태스크의 ExpiredState 를 YES 로 전환한다.
     * @param expiryDate 만료일 정보
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Task t set t.expiredState = :expiredState where t.endTime <= :expiryDate")
    void updateExpiredTask(@Param("expiryDate") LocalDate expiryDate, @Param("expiredState") ExpiredState expiredState);

    /**
     * 만료되지 않은 유저의 태스크 수를 반환한다.
     * @param userId 유저 아이디 값
     * @param expiredState 만료 상태
     * @return 태스크 수
     */
    @Query("select count(t) from Task t where t.user.id = :userId " +
            "and t.expiredState = :expiredState")
    int countOfAvailableTask(@Param("userId") Long userId,
                             @Param("expiredState") ExpiredState expiredState);


    /**
     * 해당 월에 해당되는 모든 태스크를 반환한다.
     * @param month 해당 연+월 : 2023-08
     * @param nextMonth 다음 연+월 : 2023-09
     * @param user 유저 정보
     * @param expiredState ExpiredState.NO
     * @return 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.expiredState = :expiredState " +
            "and t.startTime >= :month " +
            "and t.endTime < :nextMonth")
    List<Task> findByMonthOfTask(@Param("month") LocalDateTime month,
                                 @Param("nextMonth") LocalDateTime nextMonth,
                                 @Param("user") User user,
                                 @Param("expiredState") ExpiredState expiredState);

}
