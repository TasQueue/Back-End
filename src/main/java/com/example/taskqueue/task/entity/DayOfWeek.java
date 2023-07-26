package com.example.taskqueue.task.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "day_of_week")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DayOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_of_week_id")
    private Long id;

    @Column
    private String name;

    public DayOfWeek(String name) {
        this.name = name;
    }

}
