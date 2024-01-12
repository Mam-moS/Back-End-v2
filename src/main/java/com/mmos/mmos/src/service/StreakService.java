package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Streak;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.StreakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class StreakService {
    private final StreakRepository streakRepository;
    private final UserService userService;
    private final PlannerService plannerService;

    public List<Streak> findStreaks60Days(User user) throws BaseException {
        return streakRepository.findTop60ByUserOrderByStreakIndexDesc(user)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_STREAK));
    }

    @Transactional
    public void updateTopStreak(User user) {
        if (user.getUserTopStreak() < user.getUserCurrentStreak())
            user.updateTopStreak(user.getUserCurrentStreak());
    }

    @Transactional
    public void saveStreak(Long userIdx) throws BaseException {
        try {
            User user = userService.getUser(userIdx);
            LocalDate today = LocalDate.now();
            LocalDate beforeDay = LocalDate.now().minusDays(1);

            // 오늘자 스트릭 생성
            Streak todayStreak = null;
            Streak beforeDayStreak = null;
            boolean isExist = false;
            for (Streak streak : user.getStreaks()) {
                if (streak.getStreakDate().equals(today)) {
                    todayStreak = streak;
                    isExist = true;
                } else if (streak.getStreakDate().equals(beforeDay)) {
                    beforeDayStreak = streak;
                }
            }
            if (!isExist) {
                todayStreak = streakRepository.save(new Streak(0, today, user));
                user.addStreak(todayStreak);
            }

            // 전날 스트릭 체크 -> currentStreak 반영
            // 오늘 가입하거나 어제 스트릭 레벨이 0이라면 currentStreak 초기화
            if (beforeDayStreak != null && !beforeDayStreak.isChecked()) {
                if (beforeDayStreak.getStreakLevel() == 0) {
                    user.resetCurrentStreak();
                    beforeDayStreak.updateChecked(true);
                }
                // 어제 스트릭을 채웠다면 currentStreak + 1
                else {
                    user.plusCurrentStreak();
                    updateTopStreak(user);
                    beforeDayStreak.updateChecked(true);
                }
            } else if (beforeDayStreak == null) {
                user.resetCurrentStreak();
            }

            // 오늘자 플래너 가져오기
            Planner planner = null;
            for (Calendar userCalendar : user.getUserCalendars()) {
                // 이번 달 캘린더 가져오기
                if (userCalendar.getCalendarYear().equals(today.getYear())
                        && userCalendar.getCalendarMonth().equals(today.getMonthValue())) {
                    planner = plannerService.getPlannerByCalendarAndDate(userCalendar.getCalendarIndex(), today);
                }
            }

            // 오늘자 스트릭 패치
            if (planner != null) {
                if (planner.getPlannerDailyStudyTime() >= 5 * 60) {
                    todayStreak.updateStreak(3);
                } else if (planner.getPlannerDailyStudyTime() >= 3 * 60) {
                    todayStreak.updateStreak(2);
                } else if (planner.getPlannerDailyStudyTime() > 60) {
                    todayStreak.updateStreak(1);
                } else {
                    todayStreak.updateStreak(0);
                }
            }

        } catch (EmptyEntityException |
                 DuplicateRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Streak> getStreaks(Long userIdx) throws BaseException {
        try {
            User user = userService.getUser(userIdx);
            return findStreaks60Days(user);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
