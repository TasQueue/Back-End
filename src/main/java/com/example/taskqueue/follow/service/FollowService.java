package com.example.taskqueue.follow.service;


import com.example.taskqueue.exception.badinput.ExistFollowBadInputException;
import com.example.taskqueue.exception.badinput.FollowUserIdBadInputException;
import com.example.taskqueue.exception.badinput.LoopTaskPriorityBadInputException;
import com.example.taskqueue.exception.badinput.NotExistUserBadInputException;
import com.example.taskqueue.exception.notfound.FollowNotFoundException;
import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.entity.state.FollowState;
import com.example.taskqueue.follow.repository.FollowRepository;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * 입력된 아이디 값으로 팔로우를 찾아 반환한다.
     * @param followId 팔로우 아이디 정보
     * @return 팔로우
     */
    public Follow findById(Long followId) {
        return followRepository.findById(followId).orElseThrow(FollowNotFoundException::new);
    }

    /**
     * 팔로우를 요청(저장)한다.
     * @param follow 팔로우
     * @return 저장한 팔로우의 아이디 값
     */
    public Long saveFollow(Follow follow) {
        if(follow.getFollowUserId().equals(follow.getUser().getId())) {
            throw new FollowUserIdBadInputException();
        }
        if(userRepository.findById(follow.getFollowUserId()).isEmpty()) {
            throw new NotExistUserBadInputException();
        }
        if(!followRepository.findExistFollow(follow.getUser().getId(), follow.getFollowUserId()).isEmpty()) {
            throw new ExistFollowBadInputException();
        }
        return followRepository.save(follow).getId();
    }

    /**
     * 팔로우를 거절 또는 삭제한다.
     * @param follow 삭제할 팔로우 정보
     */
    public void deleteFollow(Follow follow) {
        followRepository.delete(follow);
    }

    /**
     * 유저가 팔로워 하는 유저들 아이디를 반환한다.
     * @param userId 팔로워 하는 유저아이디
     * @return user 가 팔로우 하는 유저 아이디 리스트
     */
    public List<Long> findFollowing(Long userId) {
        return followRepository.findFollowingById(userId);
    }

    /**
     * 유저를 팔로워 하는 유저들 아이디를 반환한다.
     * @param userId 팔로워 받는 유저아이디
     * @return user 를 팔로우 하는 유저 아이디 리스트
     */
    public List<Long> findFollower(Long userId) {
        return followRepository.findFollowerById(userId);
    }

    /**
     * 팔로우 요청(REQUEST->ACCEPT)을 수락한다.
     * @param follow 수락할 팔로우 정보
     */
    public void acceptFollow(Follow follow) {
        follow.updateFollowState(FollowState.ACCEPT);
    }

    /**
     * 유저에게 팔로우 요청한 유저들 이름들을 반환한다.
     * @param userId 팔로워 받는 유저
     * @return user 를 팔로우 하는 유저 아이디 리스트
     */
    public List<Long> findRequestFollow(Long userId) {
        return followRepository.findRequestFollowById(userId);
    }

}
