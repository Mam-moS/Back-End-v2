package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class PromotionSectionDto {
    // 인덱스
    private Long idx;
    // 제목
    private String title;
    // 스터디 이름
    private String studyName;
    // 작성 시간
    private Timestamp createdAt;
    // 수정 시간
    private Timestamp updatedAt;

    public PromotionSectionDto(Post post) {
        this.idx = post.getPostIndex();
        this.title = post.getPostTitle();
        this.studyName = post.getStudy().getStudyName();
        this.createdAt = post.getPostCreatedAt();
        this.updatedAt = post.getPostUpdatedAt();
    }
}
