package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StudyPageResponseDto {
    private Boolean studyIsComplete;
    private Long studyIndex;
    private Long userStudyIndex;
    private Integer myStatus;
    private HomeTabResponseDto home;
    private List<ProjectTabResponseDto> project;
    private SocialTabResponseDto social;
    private List<NoticeSectionDto> notices;
    private List<PromotionSectionDto> promotions;
    private List<PromotionSectionDto> myStudyPromotions;

    public StudyPageResponseDto(Boolean studyIsComplete,
                                Long studyIndex,
                                Long userStudyIndex,
                                HomeTabResponseDto home,
                                List<ProjectTabResponseDto> project,
                                SocialTabResponseDto social,
                                List<NoticeSectionDto> notices,
                                List<PromotionSectionDto> promotions,
                                List<PromotionSectionDto> myStudyPromotions,
                                Integer myStatus) {
        this.studyIsComplete = studyIsComplete;
        this.studyIndex = studyIndex;
        this.userStudyIndex = userStudyIndex;
        this.myStatus = myStatus;
        this.home = home;
        this.project = project;
        this.social = social;
        this.notices = notices;
        this.promotions = promotions;
        this.myStudyPromotions = myStudyPromotions;
    }
}
