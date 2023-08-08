package com.example.taskqueue.user.service;

import com.example.taskqueue.exception.notfound.UserNotFoundException;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
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
public class UserService {

    private final UserRepository userRepository;

    /**
     * 아이디 값으로 유저를 찾아 반환한다.
     * @param id 아이디 정보
     * @return 유저
     */
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 검색을 통해 유저 리스트를 조회한다.
     * @param name 유저 이름
     * @return 유저 리스트
     */
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * 유저 정보를 삭제한다.
     * @param user 회원 탈퇴할 유저 정보
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * 특정 유저의 고양이 상태를 전환한다.
     * @param user 유저 정보
     * @param catState 고양이 상태 정보
     */
    public void changeCatState(User user, CatState catState) {
        user.updateCatState(catState);
    }

    /**
     * 유저의 기본 정보를 갱신한다.
     * @param userId 유저 아이디 값
     * @param name 새로운 이름
     * @param intro 새로운 한 줄 소개
     * @param color 새로운 테마 색상
     */
    public void changeUser(Long userId, String name, String intro, String color) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.updateName(name);
        user.updateIntro(intro);
        user.updateThemeColor(color);
    }

    /**
     * 유저의 TotalTask 수를 1 증가시킨다.
     * @param user 유저 정보
     */
    public void plusTotalTask(User user) {
        int TT = user.getTotalTask();
        user.updateTotalTask(TT + 1);
    }


}
