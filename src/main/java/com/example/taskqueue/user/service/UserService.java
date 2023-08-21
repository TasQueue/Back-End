package com.example.taskqueue.user.service;

import com.example.taskqueue.category.repository.CategoryRepository;
import com.example.taskqueue.common.dto.SimpleTaskDto;
import com.example.taskqueue.exception.notfound.UserNotFoundException;
import com.example.taskqueue.follow.repository.FollowRepository;
import com.example.taskqueue.task.entity.Task;
import com.example.taskqueue.task.entity.state.CompleteState;
import com.example.taskqueue.task.entity.state.RepeatState;
import com.example.taskqueue.task.repository.TaskDayOfWeekRepository;
import com.example.taskqueue.task.repository.TaskRepository;
import com.example.taskqueue.task.service.TaskService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    private final FollowRepository followRepository;
    private final TaskService taskService;

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
        categoryRepository.deleteAllByUser(user);
        followRepository.deleteAllByFollower(user.getId());
        followRepository.deleteAllByUser(user);
        taskRepository.deleteAllByUser(user);
        userRepository.delete(user);
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
     * 매일 한번 씩 진행하는 유저 업데이트.
     *
     * 매일 자정 유저의 dailyUpdate 값은 false 로 초기화된다.
     * 따라서 이 업데이트를 한번 진행하여 dailyUpdate 를 true 로 바꾼 뒤에는 다음 날까지 진행하지 않는다.
     *
     * 전날 기본 태스크 모두 클리어 -> 연속 달리기 지속
     * 전날 기본 태스크 모두 클리어 X -> 0으로 초기화
     *
     * @param user
     */
    public void updateDailyState(User user, LocalDate localDate) {
        user.updateDailyUpdate(true);
        LocalDateTime present = localDate.atTime(0, 0, 0);
        LocalDateTime next = present.plusDays(1);

        boolean ALL_CLEAR =
                taskService.getTaskList(user, present, next)
                .stream()
                .anyMatch(task -> task.getCompleteState().equals(CompleteState.NO));

        int curr = user.getRunStreak();
        user.updateRunStreak(ALL_CLEAR ? curr + 1 : 0);
    }

    /**
     * [매일 자정 자동 업데이트]
     * 모든 유저의 dailyUpdate 값을 false 로 초기화한다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void dailyUpdate() {
        userRepository.dailyUpdateForUser();
    }

}
