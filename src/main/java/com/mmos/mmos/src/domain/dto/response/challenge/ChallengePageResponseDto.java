package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengePageResponseDto {

    UserInfoSectionDto userInfoSection;
    List<BadgeSectionDto> badges = new ArrayList<>();

    public ChallengePageResponseDto(Badge tier, List<Badge> myRepresentBadges, List<Badge> myBadges) {
        this.userInfoSection = new UserInfoSectionDto(tier, myRepresentBadges);

        for (Badge myBadge : myBadges) {
            boolean isRepresent = false;
            for (Badge myRepresentBadge : myRepresentBadges) {
                if(myRepresentBadge.getBadgeIndex() == myBadge.getBadgeIndex())
                    isRepresent = true;
            }
            badges.add(new BadgeSectionDto(myBadge, isRepresent));
        }
    }
}
