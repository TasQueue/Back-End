package com.example.taskqueue.task.entity;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.common.BaseEntity;
import com.example.taskqueue.task.entity.state.*;
import com.example.taskqueue.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Task extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;




    @Column()//태스크 이름
    private String name;

    @Column()//태스크 시작시간
    private LocalDateTime startTime;

    @Column()//태스크 종료시간
    private LocalDateTime endTime;

    @Column()//태스크 우선순위
    private int priority;


    @Enumerated(EnumType.STRING)//태스크 하루종일 여부
    private AllDayState allDayState;

    @Enumerated(EnumType.STRING)//태스크 달력 표시 여부 : 기본 값 = OFF
    private CalenderState calenderState;

    @Enumerated(EnumType.STRING)//태스크 수행 여부 : 기본 값 = OFF
    private CompleteState completeState;

    @Enumerated(EnumType.STRING)//루프 태스크 여부
    private RepeatState repeatState;


    //Task 내부 메서드

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePriority(int priority) {
        this.priority = priority;
    }

    public void updateStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void updateEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateAllDayState(AllDayState allDayState) {
        this.allDayState = allDayState;
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


}
