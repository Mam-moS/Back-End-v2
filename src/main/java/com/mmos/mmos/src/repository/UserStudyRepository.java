package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.Users;
import com.mmos.mmos.src.domain.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {

    Optional<UserStudy> findByUserStudyIndex(Long userStudyIdx);

    Optional<UserStudy> findUserStudyByStudyAndUser(Study study, Users user);

    Long countAllByUserAndAndUserStudyMemberStatus(Users user, Integer status);
}
