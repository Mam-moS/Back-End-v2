package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HomePageResponseDto {

    private HomeSectionDto home;
    private AchievementSectionDto achievement;
    private List<TodoSectionDto> todo = new ArrayList<>();
    private CalendarSectionDto calendar;
    private List<FriendSectionDto> friend = new ArrayList<>();

    public HomePageResponseDto(Users user,
                               List<Plan> plans,
                               CalendarSectionDto calendar,
                               List<Friend> friends,
                               Badge tier,
                               Badge nextTier,
                               List<Badge> badges,
                               Badge pfp,
                               List<Streak> streakList) {
        // HomeSection
       this.home = new HomeSectionDto(user, badges, pfp);

        // Achievement Section
        this.achievement = new AchievementSectionDto(user, tier, nextTier, streakList);

        // Today's To Do Section
        for (Plan plan : plans) {
            todo.add(new TodoSectionDto(plan));
        }

        // Calendar Section
        this.calendar = calendar;

        // Friend Section
        for (Friend myFriend : friends) {
            friend.add(new FriendSectionDto(myFriend.getFriend()));
        }
    }
}
