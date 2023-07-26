package com.example.taskqueue.follow.repository;

import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.entity.state.FollowState;
import com.example.taskqueue.follow.service.FollowService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
import com.example.taskqueue.user.repository.UserRepository;
import com.example.taskqueue.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class FollowRepositoryTest {

    @Autowired
    FollowService followService;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void followTest() {
        //given
        User user1 = new User(10L,"user1", "hi~", 10L, "yellow", CatState.ONE);
        User user2 = new User(11L, "user2", "hi~", 10L, "yellow", CatState.ONE);
        User user3 = new User(12L, "user3", "hi~", 10L, "yellow", CatState.ONE);
        User user4 = new User(13L, "user4", "hi~", 10L, "yellow", CatState.ONE);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        List<User> all = userRepository.findAll();

        Follow follow1 = new Follow(100L, all.get(0), all.get(1).getId(), FollowState.ACCEPT);
        Follow follow2 = new Follow(200L, all.get(0), all.get(2).getId(), FollowState.ACCEPT);
        Follow follow3 = new Follow(300L, all.get(2), all.get(0).getId(), FollowState.REQUEST);
        Follow follow4 = new Follow(400L, all.get(3), all.get(0).getId(), FollowState.ACCEPT);

        ///when
        followService.saveFollow(follow1);
        followService.saveFollow(follow2);
        followService.saveFollow(follow3);
        followService.saveFollow(follow4);

        List<Follow> following = followService.findFollowing(all.get(0).getId());
        List<Follow> follower = followService.findFollower(all.get(0).getId());

        //then
        for (Follow aLong : following) {
            System.out.println("following = " + userRepository.findById(aLong.getFollowUserId()).get().getName());
        }
        for (Follow aLong : follower) {
            System.out.println("follower = " + aLong.getUser().getName());
        }
    }
}