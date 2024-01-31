package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Users;
import com.mmos.mmos.src.domain.entity.UserStudy;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {
    private String name;
    private String id;
    private String universityName;
    private String majorName;
    private String status;

    public Member(UserStudy userStudy) {
        this.name = userStudy.getUser().getName();
        this.id = userStudy.getUser().getUserId();
        this.universityName = userStudy.getUser().getMajor().getCollege().getUniversity().getUniversityName();
        this.majorName = userStudy.getUser().getMajor().getMajorName();
        this.status = getStatus(userStudy.getUserStudyMemberStatus());
    }

    private String getStatus(Integer num) {
        if(num == 1) {
            return "Leader";
        }
        else if (num == 2) {
            return "Member";
        } else {
            return "";
        }
    }
}
