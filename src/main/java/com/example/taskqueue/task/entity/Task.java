package com.example.taskqueue.task.entity;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.common.BaseEntity;
import com.example.taskqueue.task.entity.state.*;
import com.example.taskqueue.user.entity.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "update task set deleted = true where task_id = ?")
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private String name;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private Boolean requiredTime;

    @Column
    private int priority;

    @Enumerated(EnumType.STRING)
    private CalenderState calenderState;

    @Enumerated(EnumType.STRING)
    private CompleteState completeState;

    @Enumerated(EnumType.STRING)
    private RepeatState repeatState;

    @Column
    private boolean deleted = false;


    @Builder
    public Task(User user,
                Category category,
                String name,
                LocalDateTime startTime,
                LocalDateTime endTime,
                int priority,
                CalenderState calenderState,
                CompleteState completeState,
                RepeatState repeatState,
                boolean requiredTime
    ) {
        this.user = user;
        this.category = category;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.calenderState = calenderState;
        this.completeState = completeState;
        this.repeatState = repeatState;
        this.requiredTime = requiredTime;
    }


    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;}
    public void updateName(String name) {
        this.name = name;
    }

    public void updateCategory(Category category) {this.category = category;}

    public void updatePriority(int priority) {
        this.priority = priority;
    }

    public void updateStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void updateEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateCalendarState(CalenderState calenderState) {
        this.calenderState = calenderState;
    }

    public void updateCompleteState(CompleteState completeState) {
        this.completeState = completeState;
    }

    public void updateRepeatState(RepeatState repeatState) {
        this.repeatState = repeatState;
    }

    public void updateRequiredTime(boolean requiredTime) {this.requiredTime = requiredTime;}


}
