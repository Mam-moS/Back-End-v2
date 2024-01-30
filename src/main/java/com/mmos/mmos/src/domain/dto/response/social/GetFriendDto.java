package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.Friend;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetFriendDto {
    private Long friendIndex;
    private String name;
    private String id;
    private String universityName;
    private String majorName;

    public GetFriendDto(Friend friend) {
        this.friendIndex = friend.getFriendIndex();
        this.name = friend.getFriend().getName();
        this.id = friend.getFriend().getUserId();
        this.universityName = friend.getFriend().getMajor().getCollege().getUniversity().getUniversityName();
        this.majorName = friend.getFriend().getMajor().getMajorName();
    }
}
