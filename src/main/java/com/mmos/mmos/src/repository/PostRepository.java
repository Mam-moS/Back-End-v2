package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findPostsByPostIsNoticeIsFalse();
    Optional<List<Post>> findPostsByPostIsNoticeIsTrue();

    Optional<List<Post>> findPostsByPostTitleContainingOrPostContentsContaining(String title, String contents);
}
