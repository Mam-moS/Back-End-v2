package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.dto.request.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.response.home.CalendarSectionDto;
import com.mmos.mmos.src.domain.dto.response.home.HomePageResponseDto;
import com.mmos.mmos.src.domain.dto.response.home.TodoSectionDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomePageController extends BaseController {

    private final UserService userService;
    private final CalendarService calendarService;
    private final FriendService friendService;
    private final PlanService planService;
    private final UserBadgeService userBadgeService;
    private final StreakService streakService;
    private final BadgeService badgeService;

    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal Users tokenUser) {
        try {
            Long userIdx = tokenUser.getUserIndex();
            // 기본 쿼리
            userBadgeService.saveUserBadge(userIdx);
            try {
                streakService.saveStreak(userIdx);
            } catch (Exception ignored) { }

            // 불러오기
            Integer year = LocalDate.now().getYear();
            Integer month = LocalDate.now().getMonthValue();

            Users user = userService.getUser(userIdx);
            CalendarSectionDto calendar = calendarService.getCalendar(userIdx,
                    new CalendarGetRequestDto(month, year));

            // Plan 찾기
            List<Plan> plans = new ArrayList<>();
            for (Calendar userCalendar : user.getUserCalendars()) {
                if (userCalendar.getCalendarMonth().equals(month) && userCalendar.getCalendarYear().equals(year))
                    for (Planner calendarPlanner : userCalendar.getCalendarPlanners()) {
                        if (calendarPlanner.getPlannerDate().equals(LocalDate.now())) {
                            int cnt = 0;
                            for (Plan plannerPlan : calendarPlanner.getPlannerPlans()) {
                                if (cnt > 9)
                                    break;
                                plans.add(plannerPlan);
                                cnt++;
                            }
                        }
                    }
            }

            // Friend 찾기
            List<Friend> friends = new ArrayList<>();
            int friendCnt = 0;
            for (Friend friend : friendService.getFriends(userIdx, 1)) {
                if(friendCnt > 9)
                    break;
                friends.add(friend);
                friendCnt++;
            }

            // 티어 찾기
            Badge tier = userBadgeService.getRepresentBadges(userIdx, "tier").get(0).getBadge();
            Badge nextTier = badgeService.getNextTier(tier.getBadgeIndex());
            // 뱃지 찾기
            List<UserBadge> userBadges = userBadgeService.getRepresentBadges(userIdx, "badge");
            List<Badge> badges = new ArrayList<>();
            for (UserBadge userBadge : userBadges) {
                badges.add(userBadge.getBadge());
            }
            // 프사 찾기
            Badge pfp = userBadgeService.getRepresentBadges(userIdx, "pfp").get(0).getBadge();
            // 최근 60일 스트릭 찾기
            List<Streak> streakList = streakService.getStreaks(userIdx);

            return sendResponseHttpByJson(SUCCESS, "페이지 로드 성공", new HomePageResponseDto(user, plans, calendar, friends, tier, nextTier, badges, pfp, streakList));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }


    } /* public ResponseEntity<ResponseApiMessage> getPage */

    /**
     * 홈 페이지 화면에서 완수한 계획들 체크할 때 보내지는 API
     * @param planIdx: 완수한 계획 인덱스
     */
    @PatchMapping("/{planIdx}")
    public ResponseEntity<ResponseApiMessage> checkTodayToDo(@PathVariable Long planIdx) {
        try {
                Plan plan = planService.updatePlanIsComplete(planIdx);
                return sendResponseHttpByJson(SUCCESS, "페이지 로드 성공", new TodoSectionDto(plan));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
