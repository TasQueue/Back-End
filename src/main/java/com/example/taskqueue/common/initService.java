package com.example.taskqueue.common;

import com.example.taskqueue.task.entity.DayOfWeek;
import com.example.taskqueue.task.repository.DayOfWeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class initService implements ApplicationListener<ContextRefreshedEvent> {

    private final DayOfWeekRepository dayOfWeekRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    /**
     * [월, 금] 요일 정보 생성 및 저장
     */
    public void insertDayOfWeek() {
        DayOfWeek MON = new DayOfWeek("MON");
        DayOfWeek TUE = new DayOfWeek("TUE");
        DayOfWeek WED = new DayOfWeek("WED");
        DayOfWeek THU = new DayOfWeek("THU");
        DayOfWeek FRI = new DayOfWeek("FRI");
        DayOfWeek SAT = new DayOfWeek("SAT");
        DayOfWeek SUN = new DayOfWeek("SUN");

        dayOfWeekRepository.save(MON);
        dayOfWeekRepository.save(TUE);
        dayOfWeekRepository.save(WED);
        dayOfWeekRepository.save(THU);
        dayOfWeekRepository.save(FRI);
        dayOfWeekRepository.save(SAT);
        dayOfWeekRepository.save(SUN);
    }


}
