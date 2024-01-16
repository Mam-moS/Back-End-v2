package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class HomeTabResponseDto {

    private String title;
    private String summary;
    private ProjectTabResponseDto projectTabResponseDto;    // 가장 최근의 project 가져오기
    private List<Member> members;

    public HomeTabResponseDto(Study study, ProjectTabResponseDto projectTabResponseDto, List<Member> members) {
        this.title = study.getStudyName();
        this.summary = study.getStudyMemo();
        this.projectTabResponseDto = projectTabResponseDto;
        this.members = members;
    }
}
