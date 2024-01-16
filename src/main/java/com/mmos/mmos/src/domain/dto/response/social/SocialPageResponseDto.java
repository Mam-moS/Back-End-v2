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
    List<RankingSectionDto> ranking = new ArrayList<>();
    List<FriendSectionDto> friend = new ArrayList<>();
    Integer friendRequestNum;

    public SocialPageResponseDto(List<Friend> friends, List<Users> top3, Integer requestNum) {
        for (Users user : top3) {
            this.ranking.add(new RankingSectionDto(user));
        }

        for (Friend user : friends) {
            this.friend.add(new FriendSectionDto(user));
        }

        this.friendRequestNum = requestNum;
    }
}
