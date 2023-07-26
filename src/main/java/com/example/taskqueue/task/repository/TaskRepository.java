package com.example.taskqueue.task.repository;

import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.CompleteState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * 특정 유저의 YES / NO 의 완료상태를 가진 태스크의 갯수를 반환한다.
     * @param userId 유저 아이디 값
     * @param completeState 유저가 얻고자 하는 태스크의 완료 상태 여부
     * @return
     */
    @Query("select count(t) from Task t where t.user.id = :userId and t.completeState = :completeState")
    int countOfCompleteTask(@Param("userId") Long userId, @Param("completeState") CompleteState completeState);

}
