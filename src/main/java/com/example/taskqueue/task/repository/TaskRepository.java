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
     * 유저의 [특정 시점 이후] 의 [모든] 일일 태스크를 모두 반환한다.
     * @param user 유저 정보
     * @param allDayState AllDayState.YES
     * @param startTime 특정 시점
     * @return 루프 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.allDayState = :allDayState " +
            "and t.startTime < :startTime")
    List<Task> findAllDayTaskByUser(
            @Param("user") User user,
            @Param("allDayState") AllDayState allDayState,
            @Param("startTime") LocalDateTime startTime
    );

    /**
     * 유저의 [캘린더 ON 상태] 일일 태스크를 모두 반환한다.
     * @param user 유저 정보
     * @param allDayState AllDayState.YES
     * @param calenderState CalenderState.YES
     * @return 루프 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.allDayState = :allDayState " +
            "and t.calenderState = :calenderState")
    List<Task> findAllDayTaskOnCalenderByUser(
            @Param("user") User user,
            @Param("allDayState") AllDayState allDayState,
            @Param("calenderState") CalenderState calenderState
    );


    /**
     * 특정 기간의 만료되지 않은 유저 [기본] 태스크를 우선순위 순으로 정렬하여 반환한다.
     * 일일 태스크 (X) 루프 태스크 (X)
     * @param user 유저 정보
     * @param expiredState ExpiredState.NO
     * @param startOfDay 기간의 시작
     * @param endOfDay 기간의 끝
     * @return 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.expiredState = :expiredState " +
            "and t.repeatState = :repeatState " +
            "and t.allDayState = :allDayState " +
            "and t.startTime >= :startOfDay " +
            "and t.endTime < :endOfDay " +
            "order by t.priority")
    List<Task> findTaskByUserAndPriority(
            @Param("user") User user,
            @Param("expiredState") ExpiredState expiredState,
            @Param("repeatState") RepeatState repeatState,
            @Param("allDayState") AllDayState allDayState,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * 특정 기간의 만료되지 않은 유저 [캘린더 ON 기본] 태스크를 우선순위 순으로 정렬하여 반환한다.
     * 일일 태스크 (X) 루프 태스크 (X)
     * @param user 유저 정보
     * @param expiredState ExpiredState.NO
     * @param startOfDay 기간의 시작
     * @param endOfDay 기간의 끝
     * @return 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user " +
            "and t.expiredState = :expiredState " +
            "and t.repeatState = :repeatState " +
            "and t.allDayState = :allDayState " +
            "and t.calenderState = :calenderState "+
            "and t.startTime >= :startOfDay " +
            "and t.endTime < :endOfDay " +
            "order by t.priority")
    List<Task> findTaskOnCalenderByUserAndPriority(
            @Param("user") User user,
            @Param("expiredState") ExpiredState expiredState,
            @Param("repeatState") RepeatState repeatState,
            @Param("allDayState") AllDayState allDayState,
            @Param("calenderState") CalenderState calenderState,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );







    //note 매일 자정 필요 기능
    /**
     * 모든 [루프 태스크] 와 [일일 태스크] 의 CompleteState 를 NO 로 전환한다.
     * @param completeState CompleteState.NO
     * @param allDayState AllDayState.YES
     * @param repeatState RepeatState.YES
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Task t " +
            "set t.completeState = :completeState " +
            "where t.allDayState = :allDayState " +
            "or t.repeatState = :repeatState")
    void resetCompleteStateOfAllTask(
            @Param("completeState") CompleteState completeState,
            @Param("allDayState") AllDayState allDayState,
            @Param("repeatState") RepeatState repeatState
    );

    /**
     * 입력받은 유저의 [루프 태스크] 와 [일일 태스크] 중 CompleteState.YES 인 태스크의 수를 반환한다.
     * @param user 유저 정보
     * @param completeState CompleteState.YES
     * @param allDayState AllDayState.YES
     * @param repeatState RepeatState.YES
     * @return
     */
    @Query("select count(t) from Task t where t.user = :user " +
            "and t.completeState = :completeState " +
            "and t.allDayState = :allDayState " +
            "or t.repeatState = :repeatState")
    Integer countOfCompleteAbNormalTask(
            @Param("user") User user,
            @Param("completeState") CompleteState completeState,
            @Param("allDayState") AllDayState allDayState,
            @Param("repeatState") RepeatState repeatState
    );

    /**
     * 유저의 [루프 태스크] + [일반 태스크] 의 갯수를 반환한다.
     * @param user 유저 정보
     * @param allDayState AllDayState.YES
     * @param repeatState RepeatState.YES
     * @return
     */
    @Query("select count(t) from Task t where t.user = :user " +
            "and t.completeState = :completeState " +
            "and (t.allDayState = :allDayState " +
            "or t.repeatState = :repeatState)")
    Integer countOfAbNormalTask(
            @Param("user") User user,
            @Param("allDayState") AllDayState allDayState,
            @Param("repeatState") RepeatState repeatState
    );


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
    Integer countOfCompleteTask(@Param("userId") Long userId,
                            @Param("completeState") CompleteState completeState,
                            @Param("expiredState") ExpiredState expiredState);

    /**
     * 모든 시스템 유저의 만료일이 지난 [일반] 태스크의 ExpiredState 를 YES 로 전환한다.
     * 일일 태스크(X)
     * 루프 태스크(X)
     * 따라서 위의 두 태스크 종류는 항상 ExpiredState.NO 의 상태다.
     * @param expiryDate 만료일 정보
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Task t set t.expiredState = :expiredState " +
            "where t.endTime <= :expiryDate " +
            "and t.repeatState = :repeatState " +
            "and t.allDayState = :allDayState")
    void updateExpiredTask(@Param("expiryDate") LocalDate expiryDate,
                           @Param("expiredState") ExpiredState expiredState,
                           @Param("repeatState") RepeatState repeatState,
                           @Param("allDayState") AllDayState allDayState);

    /**
     * 만료되지 않은 유저의 태스크 수를 반환한다.
     * [일일 태스크 수] + [루프 태스크 수] + [만료되지 않은 일반 태스크 수]
     *
     * @param userId 유저 아이디 값
     * @param expiredState 만료 상태
     * @return 태스크 수
     */
    @Query("select count(t) from Task t where t.user.id = :userId " +
            "and t.expiredState = :expiredState")
    Integer countOfAvailableTask(@Param("userId") Long userId,
                             @Param("expiredState") ExpiredState expiredState);


}
