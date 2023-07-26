package com.example.taskqueue.follow.repository;


import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 유저가 팔로워 하는 유저들 아이디를 반환한다.
     * @param userId 팔로워 하는 유저
     * @return user 가 팔로우 하는 유저 아이디 리스트
     */
    @Query("select f from Follow f where f.user.id = :userId and f.followState = com.example.taskqueue.follow.entity.state.FollowState.ACCEPT")
    List<Follow> findFollowingById(@Param("userId") Long userId);

    /**
     * 유저를 팔로워 하는 유저들 이름들을 반환한다.
     * @param userId 팔로워 받는 유저
     * @return user 를 팔로우 하는 유저 이름 리스트
     */
    @Query("select f from Follow f where f.followUserId = :userId and f.followState = com.example.taskqueue.follow.entity.state.FollowState.ACCEPT")
    List<Follow> findFollowerById(@Param("userId") Long userId);
}
