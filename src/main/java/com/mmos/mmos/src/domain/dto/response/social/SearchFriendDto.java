package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SearchFriendDto {

    private String name;
    private String id;
    private String universityName;
    private String collegeName;

    public SearchFriendDto(Users user) {
        this.name = user.getName();
        this.id = user.getUserId();
        this.universityName = user.getMajor().getCollege().getUniversity().getUniversityName();
        this.collegeName = user.getMajor().getMajorName();
    }
}
