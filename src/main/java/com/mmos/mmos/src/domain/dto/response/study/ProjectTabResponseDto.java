package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectTabResponseDto {
    private String projectTitle;
    private String studySummary;
    private LocalDate startDate;
    private LocalDate endDate;
    private String place;
    private List<Member> projectInMembers;

    public ProjectTabResponseDto(Project project, List<Member> members) {
        this.projectTitle = project.getProjectName();
        this.studySummary = project.getProjectMemo();
        this.startDate = project.getProjectStartTime();
        this.endDate = project.getProjectEndTime();
        this.place = project.getProjectPlace();
        this.projectInMembers = members;
    }

    public ProjectTabResponseDto(Project project) {
        this.projectTitle = project.getProjectName();
        this.studySummary = project.getProjectMemo();
        this.startDate = project.getProjectStartTime();
        this.endDate = project.getProjectEndTime();
        this.place = project.getProjectPlace();
    }

}
