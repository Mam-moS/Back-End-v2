package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.Friend;
import com.mmos.mmos.src.domain.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SocialPageResponseDto {
    private Long userIdx;
    private List<RankingSectionDto> ranking = new ArrayList<>();
    private List<FriendSectionDto> friend = new ArrayList<>();
    private Integer friendRequestNum;

    public SocialPageResponseDto(Long userIdx, List<Friend> friends, List<Users> top3, Integer requestNum) {
        this.userIdx = userIdx;

        for (Users user : top3) {
            this.ranking.add(new RankingSectionDto(user));
        }

        for (Friend user : friends) {
            this.friend.add(new FriendSectionDto(user));
        }

        this.friendRequestNum = requestNum;
    }
}
