package com.example.taskqueue.follow.entity;

import com.example.taskqueue.follow.entity.state.FollowState;
import com.example.taskqueue.user.entity.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "update follow set deleted = true where follow_id = ?")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long followUserId;

    @Enumerated(EnumType.STRING)
    private FollowState followState;

    @Column
    private boolean deleted = false;

    @Builder
    public Follow(
            User user,
            Long followUserId,
            FollowState followState
    ) {
        this.user = user;
        this.followUserId = followUserId;
        this.followState = followState;
    }


    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;}

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateFollowUserId(Long followUserId) {
        this.followUserId = followUserId;
    }

    public void updateFollowState(FollowState followState) {
        this.followState = followState;
    }


}
