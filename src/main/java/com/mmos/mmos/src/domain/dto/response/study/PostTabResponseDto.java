package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostTabResponseDto {

    List<NoticeSectionDto> notices;

    List<PromotionSectionDto> promotions;

    public PostTabResponseDto(List<NoticeSectionDto> notices, List<PromotionSectionDto> promotions) {
        this.notices = notices;
        this.promotions = promotions;
    }
}
