package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengePageResponseDto {

    UserInfoSectionDto userInfoSection;
    List<BadgeSectionDto> badges = new ArrayList<>();

    public ChallengePageResponseDto(Badge tier, List<Badge> myRepresentBadges, List<Badge> myBadges, Users user, Badge nextTier) {
        this.userInfoSection = new UserInfoSectionDto(tier, myRepresentBadges, user, nextTier);

        for (Badge myBadge : myBadges) {
            boolean isRepresent = false;
            for (Badge myRepresentBadge : myRepresentBadges) {
                if (myRepresentBadge.getBadgeIndex().equals(myBadge.getBadgeIndex())) {
                    isRepresent = true;
                    break;
                }
            }
            badges.add(new BadgeSectionDto(myBadge, isRepresent));
        }
    }
}
