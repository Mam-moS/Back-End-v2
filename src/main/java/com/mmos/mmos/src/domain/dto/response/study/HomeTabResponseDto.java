package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HomeTabResponseDto {

    private String title;
    private String summary;
    private ProjectTabResponseDto projectTabResponseDto;    // 가장 최근의 project 가져오기
    private List<Member> members;
    private List<NoticeSectionDto> notices = new ArrayList<>();

    public HomeTabResponseDto(Study study, ProjectTabResponseDto projectTabResponseDto, List<Member> members, List<Post> notices) {
        this.title = study.getStudyName();
        this.summary = study.getStudyMemo();
        this.projectTabResponseDto = projectTabResponseDto;
        this.members = members;

        for (Post notice : notices) {
            this.notices.add(new NoticeSectionDto(notice));
        }
    }
}
