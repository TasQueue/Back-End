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
    @ColumnDefault(value = "0")
    private int runStreak;

    @Column
    @ColumnDefault(value = "false")
    private Boolean dailyUpdate;

    @Column
    //@ColumnDefault(value = "#C2D9FA")
    private String themeColor = "#C2D9FA";

    @Column
    private CatState catState;

    @Column
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String socialId;


    public void updateName(String name) {
        this.name = name;
    }
    public void updateRunStreak(int runStreak) {this.runStreak = runStreak;}
    public void updateDailyUpdate(Boolean dailyUpdate) {this.dailyUpdate = dailyUpdate;}
    public void updateIntro(String intro) {
        this.intro = intro;
    }
    public void updateCatState(CatState catState) {
        this.catState = catState;
    }
    public void updateThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }
    public void updateRefreshToken(String updateRefreshToken){ this.refreshToken = updateRefreshToken; }
    public void updateEmail(String updateEmail){ this.email = updateEmail; }
}
