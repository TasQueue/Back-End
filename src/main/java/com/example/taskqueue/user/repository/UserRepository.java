package com.example.taskqueue.user.repository;

import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 입력받은 이름을 통해 유저 리스트를 조회한다.
     * @param name 검색할 유저 이름
     * @return 유저 리스트
     */
    List<User> findByName(String name);

    /**
     * 매일 자정 실행되는 업데이트
     * 모든 유저의 dailyUpdate 값을 false 로 전환한다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u " +
            "set u.dailyUpdate = false " +
            "where u.deleted = false")
    void dailyUpdateForUser();


    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findBySocialId(String id);
}
