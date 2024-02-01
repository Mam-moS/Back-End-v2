package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class StudyPageResponseDto {

    private Long userStudyIndex;
    private HomeTabResponseDto home;
    private List<ProjectTabResponseDto> project;
    private SocialTabResponseDto social;
    private PostTabResponseDto post;

    public StudyPageResponseDto(Long userStudyIndex, HomeTabResponseDto home, List<ProjectTabResponseDto> project, SocialTabResponseDto social, PostTabResponseDto post) {
        this.userStudyIndex = userStudyIndex;
        this.home = home;
        this.project = project;
        this.social = social;
        this.post = post;
    }
}
