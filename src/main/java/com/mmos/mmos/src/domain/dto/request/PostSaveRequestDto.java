package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String postTitle;
    private String postContents;
    private String postImage;
    private Boolean isNotice;
    private List<Long> fileIndex;
}
