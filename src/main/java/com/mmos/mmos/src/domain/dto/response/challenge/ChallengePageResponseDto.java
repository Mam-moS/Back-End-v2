package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengePageResponseDto {

    UserInfoSectionDto userInfoSection;
    BadgeSectionDto badgeSection;

    public ChallengePageResponseDto(Badge tier, List<Badge> myRepresentBadges, List<Badge> myBadges) {
        this.userInfoSection = new UserInfoSectionDto(tier, myRepresentBadges);
        this.badgeSection = new BadgeSectionDto(myBadges);
    }
}
