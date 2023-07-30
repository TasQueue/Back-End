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
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    /**
     * 만료되지 않은 유저의 태스크를 우선순위 순으로 정렬하여 반환한다.
     * @param user 유저 정보
     * @param expiredState ExpiredState.NO
     * @return 태스크 리스트
     */
    @Query("select t from Task t where t.user = :user and t.expiredState = :expiredState order by t.priority")
    List<Task> findTaskByUserAndPriority(@Param("user") User user, @Param("expiredState") ExpiredState expiredState);

    /**
     * 특정 유저의 YES / NO 의 완료상태를 가진 태스크의 갯수를 반환한다.
     * @param userId 유저 아이디 값
     * @param completeState 유저가 얻고자 하는 태스크의 완료 상태 여부
     * @return
     */
    @Query("select count(t) from Task t where t.user.id = :userId and t.completeState = :completeState")
    int countOfCompleteTask(@Param("userId") Long userId, @Param("completeState") CompleteState completeState);

    /**
     * 모든 시스템 유저의 만료일이 지난 태스크의 ExpiredState 를 YES 로 전환한다.
     * @param expiryDate 만료일 정보
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Task t set t.expiredState = :expiredState where t.endTime <= :expiryDate")
    void updateExpiredTask(@Param("expiryDate") LocalDate expiryDate, @Param("expiredState") ExpiredState expiredState);


    /**
     * 매일 자정 만료될 유저의 태스크 갯수를 반환한다.
     * @param expiryDate 만료일
     * @param userId 유저 아이디 값
     * @return
     */
    @Query("select count(t) from Task t where t.endTime <= :expiryDate and t.user.id = :userId and t.expiredState = :expiredState")
    int countOfExpiredTask(@Param("expiryDate") LocalDate expiryDate,
                           @Param("userId") Long userId,
                           @Param("expiredState") ExpiredState expiredState);


    /**
     * 만료되지 않은 유저의 태스크 수를 반환한다.
     * @param userId 유저 아이디 값
     * @param expiredState 만료 상태
     * @return
     */
    @Query("select count(t) from Task t where t.user.id = :userId and t.expiredState = :expiredState")
    int countOfAvailableTask(@Param("userId") Long userId,
                             @Param("expiredState") ExpiredState expiredState);


}
