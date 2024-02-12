package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.OutOfRangeException;
import com.mmos.mmos.src.domain.dto.request.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.response.post.PostPageResponseDto;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.domain.entity.Users;
import com.mmos.mmos.src.service.PostService;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.FILE_LIMIT_OVER;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostPageController extends BaseController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/{postIdx}")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal Users tokenUser, @PathVariable Long postIdx) {
        try {
            Post post = postService.getPost(postIdx);
            Users user = userService.getUser(tokenUser.getUserIndex());

            boolean isLeader = false;
            for (UserStudy postUserStudy : post.getStudy().getStudyUserstudies()) {
                for (UserStudy userUserstudy : user.getUserUserstudies()) {
                    if(userUserstudy.equals(postUserStudy)) {
                        if (postUserStudy.getUserStudyMemberStatus() == 1) {
                            isLeader = true;
                        }
                    }
                }
            }

            return sendResponseHttpByJson(SUCCESS, "글 조회 성공", new PostPageResponseDto(post, isLeader));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PatchMapping("/{postIdx}/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> updatePost(@PathVariable Long postIdx,
                                                         @PathVariable Long userStudyIdx,
                                                         @RequestPart(required = false) List<MultipartFile> multipartFiles,
                                                         @RequestPart PostSaveRequestDto requestDto) {
        try {
            if (multipartFiles != null && multipartFiles.size() > 5)
                throw new OutOfRangeException(FILE_LIMIT_OVER);

            return sendResponseHttpByJson(SUCCESS, "글 수정 성공",
                    postService.updatePost(postIdx, userStudyIdx, requestDto, multipartFiles));
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @DeleteMapping("/{postIdx}/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> deletePost(@PathVariable Long postIdx,
                                                         @PathVariable Long userStudyIdx) {
        try {
            postService.deletePost(postIdx, userStudyIdx);
            return sendResponseHttpByJson(SUCCESS, "글 삭제 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
