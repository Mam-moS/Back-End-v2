package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class StudyPageResponseDto {

    HomeTabResponseDto home;
    List<ProjectTabResponseDto> project;
    SocialTabResponseDto social;
    PostTabResponseDto post;

    public StudyPageResponseDto(HomeTabResponseDto home, List<ProjectTabResponseDto> project, SocialTabResponseDto social, PostTabResponseDto post) {
        this.home = home;
        this.project = project;
        this.social = social;
        this.post = post;
    }
}
