package com.example.taskqueue.user.repository;

import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
}
