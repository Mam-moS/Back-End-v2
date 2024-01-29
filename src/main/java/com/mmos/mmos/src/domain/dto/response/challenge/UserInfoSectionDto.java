package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserInfoSectionDto {

    // 티어
    Badge tier;
    // 대표 도전과제
    List<Badge> badges = new ArrayList<>();
    // 티어 진행도
    Integer tierProgress;


    public UserInfoSectionDto(Badge tier, List<Badge> badges, Users user, Badge nextTier) {
        this.tier = tier;
        this.badges = badges;

        if(user.getUserTotalScheduleNum().equals(0L)){
            this.tierProgress = (int) (user.getUserTotalStudyTime() * 4);
        } else {
            this.tierProgress = (int) (((user.getUserTotalStudyTime() * 4) // 공부시간 * 4
                    + ((100 + user.getUserTotalCompletedScheduleNum()/user.getUserTotalScheduleNum() * 100) / 100) * user.getUserTotalCompletedScheduleNum()) * 100 // (1 + 완수율) * 완수 개수
                    / nextTier.getBadgeExp());  // 획득 EXP / 티어 EXP * 100
        }
    }
}
