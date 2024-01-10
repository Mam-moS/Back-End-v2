package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.service.FileService;
import com.mmos.mmos.src.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
@RestController
public class FileController extends BaseController {

    private final FileService fileService;
    private final PostService postService;

    @PostMapping("/{postIdx}")
    public ResponseEntity<ResponseApiMessage> uploadFile(@RequestParam MultipartFile multipartFile, @PathVariable Long postIdx) {
        try {
            Post post = postService.getPost(postIdx);

            return sendResponseHttpByJson(SUCCESS, "사진 업로드 성공", fileService.uploadFile(multipartFile, post));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
