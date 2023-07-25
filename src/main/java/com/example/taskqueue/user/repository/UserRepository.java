package com.example.taskqueue.user.repository;

import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { //TODO 임의로 추가한 findBy..
    Optional<User> findByEmail(String email);
}
