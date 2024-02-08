package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.UserStudy;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetStudyListDto {

    private Long userStudyIdx;
    private String studyName;
    private Integer maxMemberCnt;
    private Integer currentMemberCnt;

    public GetStudyListDto(UserStudy userStudy) {
        this.userStudyIdx = userStudy.getUserStudyIndex();
        this.studyName = userStudy.getStudy().getStudyName();
        this.maxMemberCnt = userStudy.getStudy().getStudyMemberLimit();
        this.currentMemberCnt = userStudy.getStudy().getStudyMemberNum();
    }
}
