package com.example.taskqueue.follow.entity;

import com.example.taskqueue.follow.entity.state.FollowState;
import com.example.taskqueue.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
