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

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateFollowUserId(Long followUserId) {
        this.followUserId = followUserId;
    }

    public void updateFollowState(FollowState followState) {
        this.followState = followState;
    }

    /**
     * 저장할 팔로우(요청상태)를 생성한다.
     * @param user 팔로우 하는 유저
     * @param followUserId 팔로우 할 유저 아이디
     * @return 만들어진 팔로우
     */
    public static Follow createFollow(User user, Long followUserId) {
        Follow follow = new Follow();
        follow.updateUser(user);
        follow.updateFollowUserId(followUserId);
        follow.updateFollowState(FollowState.REQUEST);
        return follow;
    }
}
