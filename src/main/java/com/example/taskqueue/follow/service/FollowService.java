package com.example.taskqueue.follow.service;


import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.entity.state.FollowState;
import com.example.taskqueue.follow.repository.FollowRepository;
import com.example.taskqueue.user.entity.User;
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

    /**
     * 입력된 아이디 값으로 팔로우를 찾아 반환한다.
     * @param id 아이디 정보
     * @return 팔로우
     */
    public Follow findById(Long id) {
        return followRepository.findById(id).orElseThrow();
    }

    /**
     * 팔로우를 생성한다.
     * @param follow 저장할 팔로우 정보
     * @return 저장한 팔로우의 아이디 값
     */
    public Long saveFollow(Follow follow) {
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
        return followRepository.getFollowingIdList(userId);
    }

    /**
     * 유저를 팔로워 하는 유저들 아이디를 반환한다.
     * @param userId 팔로워 받는 유저아이디
     * @return user 를 팔로우 하는 유저 이름 리스트
     */
    public List<Long> findFollower(Long userId) {
        return followRepository.getFollowerIdList(userId);
    }

    /**
     * 팔로우 요청(REQUEST->ACCEPT)을 수락한다.
     * @param follow 수락할 팔로우 정보
     */
    public void acceptFollowAsk(Follow follow) {
        follow.updateFollowState(FollowState.ACCEPT);
    }


    //TODO 팔로잉 팔로워 정보 조회

    //TODO 팔로워 수락

    //TODO 팔로워 삭제

    //TODO 팔로우 검색

    //TODO 팔로우 상세 정보

    //TODO 팔로우 요청

    //TODO 팔로우 삭제



}
