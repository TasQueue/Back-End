package com.example.taskqueue.task.entity;

import com.example.taskqueue.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Task extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;


    @Column()
    private String name;


    @Column()
    private LocalDateTime startTime;

    @Column()
    private LocalDateTime endTime;

}
