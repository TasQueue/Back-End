package com.example.taskqueue.user.entity;


import com.example.taskqueue.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column()
    private String intro;

    @Column()
    @ColumnDefault("0")
    private Long totalTask;

    @Column()
    private String themeColor;




    //태스크 수 증가
    public void addTask() {
        this.totalTask++;
    }

    //태스크 수 감소
    public void deleteTask() {
        this.totalTask--;
    }

    //유저 한 줄 소개 변경
    public void updateIntro(String intro) {
        this.intro = intro;
    }



}
