package com.example.taskqueue.user.entity;


import com.example.taskqueue.common.BaseEntity;
import com.example.taskqueue.user.entity.state.CatState;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String intro;

    @Column
    @ColumnDefault("0")
    private int totalTask;

    @Column
    private String themeColor;

    @Column
    private CatState catState;

    @Column
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String socialId;


    //유저 이름 변경
    public void updateName(String name) {
        this.name = name;
    }

    public void updateTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

    //유저 한 줄 소개 변경
    public void updateIntro(String intro) {
        this.intro = intro;
    }

    //유저 고양이 상태 변경
    public void updateCatState(CatState catState) {
        this.catState = catState;
    }

    //유저 잔디 색깔 변경(테마 컬러)
    public void updateThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public void updateRefreshToken(String updateRefreshToken){ this.refreshToken = updateRefreshToken; }
    public void updateEmail(String updateEmail){ this.email = updateEmail; }
}
