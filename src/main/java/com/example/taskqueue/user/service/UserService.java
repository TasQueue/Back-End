package com.example.taskqueue.user.service;

import com.example.taskqueue.exception.notfound.UserNotFoundException;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
