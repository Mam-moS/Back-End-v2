package com.mmos.mmos.src.domain.dto.response.challenge;

import com.mmos.mmos.src.domain.entity.Badge;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BadgeSectionDto {

    Badge myBadges;
    boolean isRepresent;

    public BadgeSectionDto(Badge myBadges, boolean isRepresent) {
        this.myBadges = myBadges;
        this.isRepresent = isRepresent;
    }
}
