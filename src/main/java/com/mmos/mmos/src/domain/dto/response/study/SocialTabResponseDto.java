package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SocialTabResponseDto {
    private List<Member> members;
    private Integer memberCnt;

    public SocialTabResponseDto(List<Member> members, Integer memberCnt) {
        this.members = members;
        this.memberCnt = memberCnt;
    }
}
