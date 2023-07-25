package com.example.taskqueue.user.entity;


import com.example.taskqueue.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor() // TODO 임의로 생성자 주입함(기민) access = AccessLevel.PROTECTED
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private String email;
    @Column()
    private String intro;


    @Column()
    @ColumnDefault("0")
    private Long totalTask;

    @Column()
    private String themeColor;



    @Builder
    public User(String name,String email, Role role) {
        this.name = name;
        this.email = email;
        this.role=role;
        this.intro = "";
        this.totalTask =0L;
        this.themeColor = "";
    }
    public User update(String name){
        this.name = name;
        return this;
    }

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


    public String getRoleKey() {
        return role.getKey();
    }
}
