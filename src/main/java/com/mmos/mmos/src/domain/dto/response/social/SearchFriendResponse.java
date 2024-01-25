package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SearchFriendResponse {
    private String name;
    private String id;
    private String universityName;
    private String majorName;

    public SearchFriendResponse(Users users) {
        this.name = users.getName();
        this.id = users.getUserId();
        this.universityName = users.getMajor().getCollege().getUniversity().getUniversityName();
        this.majorName = users.getMajor().getMajorName();
    }
}
