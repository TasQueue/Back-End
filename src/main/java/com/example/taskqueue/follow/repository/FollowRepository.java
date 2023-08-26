package com.example.taskqueue.follow.repository;


import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 유저가 팔로워 하는 유저들 아이디를 반환한다.
     * @param userId 팔로워 하는 유저
     * @return user 가 팔로우 하는 유저 아이디 리스트
     */
    @Query("select f.followUserId " +
            "from Follow f " +
            "inner join User u on f.user = u and u.deleted = false " +
            "where f.user.id = :userId " +
            "and f.followState = com.example.taskqueue.follow.entity.state.FollowState.ACCEPT")
    List<Long> findFollowingById(@Param("userId") Long userId);

    /**
     * 유저를 팔로워 하는 유저들 아이디를 반환한다.
     * @param userId 팔로워 받는 유저
     * @return user 를 팔로우 하는 유저 아이디 리스트
     */
    @Query("select f.user.id " +
            "from Follow f " +
            "inner join User u on f.user = u and u.deleted = false " +
            "where f.followUserId = :userId " +
            "and f.followState = com.example.taskqueue.follow.entity.state.FollowState.ACCEPT")
    List<Long> findFollowerById(@Param("userId") Long userId);

    /**
     * 유저에게 팔로우 요청한 유저들 이름들을 반환한다.
     * @param userId 팔로워 받는 유저
     * @return user 를 팔로우 하는 유저 아이디 리스트
     */
    @Query("select f.user.id " +
            "from Follow f " +
            "inner join User u on f.user = u and u.deleted = false " +
            "where f.followUserId = :userId " +
            "and f.followState = com.example.taskqueue.follow.entity.state.FollowState.REQUEST")
    List<Long> findRequestFollowById(@Param("userId") Long userId);

    @Query("select f.id " +
            "from Follow f " +
            "inner join User u on f.user = u and u.deleted = false " +
            "where f.user.id = :userId " +
            "and f.followUserId = :followUserId")
    List<Long> findExistFollow(@Param("userId") Long userId, @Param("followUserId") Long followUserId);


    /**
     * 특정 유저가 팔로우하는 Follow 정보를 모두 삭제한다.
     * @param user 유저 정보
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Follow f " +
            "set f.deleted = true " +
            "where f.user = :user")
    void deleteAllByUser(@Param("user") User user);


    /**
     * 특정 유저를 팔로우하는 Follow 정보를 모두 삭제한다.
     * @param userId 특정 유저의 아이디 값
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Follow f " +
            "set f.deleted = true " +
            "where f.followUserId = :userId")
    void deleteAllByFollower(@Param("userId") Long userId);
}
