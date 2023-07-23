package com.example.taskqueue.task.repository;

import com.example.taskqueue.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


}
