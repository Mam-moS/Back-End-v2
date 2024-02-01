package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class FriendSectionDto {
    // 친구 객체 인덱스
    private Long friendIndex;
    // 친구 유저 인덱스
    private Long friendUserIndex;
    // 티어
    private String tier;
    // 프사
    private String pfp;
    // 이름
    private String name;
    // 아이디
    private String id;
    // 이번 주 공부 시간
    private Long weeklyStudyTime;
    // 오늘 공부 시간
    private Long dailyStudyTime;
    // 소속 대학명
    private String universityName;
    // 소속 전공
    private String majorName;
    // 접속 여부
    private Boolean isOnline = false;
    // 고정 친구 여부
    private Boolean isFixed;

    public FriendSectionDto(Friend user) {
        this.friendIndex = user.getFriendIndex();
        for (UserBadge userUserbadge : user.getFriend().getUserUserbadges()) {
            if (userUserbadge.getBadge().getBadgePurpose().equals("pfp") && userUserbadge.getUserbadgeIsVisible())
                this.pfp = userUserbadge.getBadge().getBadgeIcon();
            if (userUserbadge.getBadge().getBadgePurpose().equals("tier") && userUserbadge.getUserbadgeIsVisible())
                this.tier = userUserbadge.getBadge().getBadgeIcon();
        }
        this.friendUserIndex = user.getFriend().getUserIndex();
        this.name = user.getFriend().getName();
        this.id = user.getFriend().getUserId();
        this.weeklyStudyTime = user.getFriend().getUserWeeklyStudyTime();
        this.isFixed = user.getFriendIsFixed();
        this.universityName = user.getFriend().getMajor().getCollege().getUniversity().getUniversityName();
        this.majorName = user.getFriend().getMajor().getMajorName();
        for (Calendar userCalendar : user.getFriend().getUserCalendars()) {
            if(userCalendar.getCalendarYear().equals(LocalDate.now().getYear()) &&
                    userCalendar.getCalendarMonth().equals(LocalDate.now().getMonthValue()))
                for (Planner calendarPlanner : userCalendar.getCalendarPlanners()) {
                    if(calendarPlanner.getPlannerDate().equals(LocalDate.now())) {
                        this.dailyStudyTime = calendarPlanner.getPlannerDailyStudyTime();
                        for (Plan plannerPlan : calendarPlanner.getPlannerPlans()) {
                            if (plannerPlan.getStudytimeStartTime() != null) {
                                isOnline = true;
                                break;
                            }
                        }
                    }
                }
        }
    }
}
