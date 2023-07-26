package com.example.taskqueue.task.service;

import com.example.taskqueue.exception.notfound.DayNotFoundException;
import com.example.taskqueue.task.entity.DayOfWeek;
import com.example.taskqueue.task.repository.DayOfWeekRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DayOfWeekService {

    private final DayOfWeekRepository dayOfWeekRepository;

    /**
     * 입력받은 아이디 값의 요일을 반환한다.
     * @param id 요일 아이디 값
     * @return 요일
     */
    public DayOfWeek findById(Long id) {
        return dayOfWeekRepository.findById(id).orElseThrow(DayNotFoundException::new);
    }

    /**
     * 입력받은 이름의 요일을 반환한다.
     * @param name 요일 이름 값
     * @return 요일
     *
     * MON, TUE, WED, THU, FRI, SAT, SUN
     */
    public DayOfWeek findByName(String name) {
        return dayOfWeekRepository.findDayOfWeekByName(name);
    }

}
