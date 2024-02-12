package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.config.exception.NotAuthorizedAccessException;
import com.mmos.mmos.src.domain.dto.request.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.response.study.NoticeSectionDto;
import com.mmos.mmos.src.domain.dto.response.study.PostTabResponseDto;
import com.mmos.mmos.src.domain.dto.response.study.PromotionSectionDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserStudyService userStudyService;
    private final FileService fileService;

    public Post findPostByIdx(Long postIdx) throws BaseException {
        return postRepository.findById(postIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_POST));
    }

    public List<Post> findPostsByPromotionPost() throws BaseException {
        return postRepository.findPostsByPostIsNoticeIsFalse()
                .orElseThrow(() -> new EmptyEntityException(EMPTY_POST));
    }

    public List<Post> findNoticesTop5(Study study) throws BaseException {
        return postRepository.findTop5ByStudyAndPostIsNoticeIsTrueOrderByPostIndexDesc(study);
    }

    public List<Post> searchPromotion(String searchStr) throws BaseException {
        return postRepository.findPostsByPostTitleContainingOrPostContentsContaining(searchStr, searchStr)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_POST));
    }

    @Transactional
    public Post savePost(Long userStudyIdx, PostSaveRequestDto postSaveRequestDto) throws BaseException {
        try {
            UserStudy userStudy = userStudyService.getUserStudy(userStudyIdx);
            if (!userStudy.getUserStudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            Users user = userStudy.getUser();
            Study study = userStudy.getStudy();

            // Post 생성/매핑
            Post post = new Post(postSaveRequestDto, user, study, new Timestamp(System.currentTimeMillis()), null);
            study.addPost(post);

            return postRepository.save(post);
        } catch (EmptyEntityException |
                 NotAuthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Post updatePost(Long postIdx, Long userStudyIdx, PostSaveRequestDto requestDto, List<MultipartFile> multipartFiles) throws BaseException {
        try {
            UserStudy userStudy = userStudyService.getUserStudy(userStudyIdx);
            if (!userStudy.getUserStudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            Post post = findPostByIdx(postIdx);

            for (Files file : post.getFiles()) {
                System.out.println("delete " + file.getStoreFileUrl());
                fileService.deleteFile(file);
            }

            boolean isEdit = false;
            if (requestDto.getPostTitle() != null) {
                post.updateTitle(requestDto.getPostTitle().toLowerCase());
                isEdit = true;
            }
            if (requestDto.getPostContents() != null) {
                post.updateContents(requestDto.getPostContents().toLowerCase());
                isEdit = true;
            }
            if (isEdit) {
                post.updateWriter(userStudy.getUser());
                post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            }

            if (multipartFiles != null) {
                for (MultipartFile multipartFile : multipartFiles) {
                    if (!multipartFile.isEmpty()) {
                        Files file = fileService.uploadFile(multipartFile, post);
                        post.addFile(file);
                    }
                }
            }

            return post;
        } catch (EmptyEntityException |
                 NotAuthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 게시글 단일 조회
    @Transactional
    public Post getPost(Long postIdx) throws BaseException {
        try {
            return findPostByIdx(postIdx);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 홍보 게시글 전체 조회
    @Transactional
    public List<PromotionSectionDto> getPromotions() throws BaseException {
        try {
            List<PromotionSectionDto> result = new ArrayList<>();
            List<Post> posts = findPostsByPromotionPost();

            for (Post post : posts) {
                result.add(new PromotionSectionDto(post));
            }

            return result;
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PostTabResponseDto getStudyPosts(UserStudy userStudy) throws BaseException {
        try {
            List<NoticeSectionDto> notices = new ArrayList<>();
            List<PromotionSectionDto> promotions = new ArrayList<>();
            Study study = userStudy.getStudy();

            for (Post studyPost : study.getStudyPosts()) {
                if (studyPost.getPostIsNotice())
                    notices.add(new NoticeSectionDto(studyPost));
                else
                    promotions.add(new PromotionSectionDto(studyPost));
            }

            return new PostTabResponseDto(notices, promotions);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postIdx, Long userStudyIdx) throws BaseException {
        try {
            UserStudy userStudy = userStudyService.getUserStudy(userStudyIdx);
            if (!userStudy.getUserStudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            Post post = findPostByIdx(postIdx);
            postRepository.delete(post);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Page<Post> searchPromotionByTitleAndContents(String searchStr, Pageable pageable) throws BaseException {
        try {
            List<Post> posts = searchPromotion(searchStr);
            return new PageImpl<>(posts, pageable, posts.size());
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
