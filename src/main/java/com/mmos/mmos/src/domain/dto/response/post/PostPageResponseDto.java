package com.mmos.mmos.src.domain.dto.response.post;

import com.mmos.mmos.src.domain.entity.Files;
import com.mmos.mmos.src.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostPageResponseDto {

    private Post post;
    private List<Files> images;
    private boolean isLeader;

    public PostPageResponseDto(Post post, boolean isLeader) {
        this.post = post;
        images = post.getFiles();
        this.isLeader = isLeader;
    }
}
