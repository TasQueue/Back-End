package com.example.taskqueue.user.repository;

import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 입력받은 이름을 통해 유저 리스트를 조회한다.
     * @param name 검색할 유저 이름
     * @return 유저 리스트
     */
    List<User> findByName(String name);

    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findBySocialId(String id);
}
