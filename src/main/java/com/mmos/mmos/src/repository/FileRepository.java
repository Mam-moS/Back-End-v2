package com.mmos.mmos.src.repository;


import com.mmos.mmos.src.domain.entity.Files;
import com.mmos.mmos.src.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<Files, Long> {

    Optional<List<Files>> findAllByPost(Post post);
}
