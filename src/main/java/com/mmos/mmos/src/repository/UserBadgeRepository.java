package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.UserBadge;
import com.mmos.mmos.src.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    Optional<List<UserBadge>> findUserBadgesByUser_UserIndexAndBadge_BadgePurpose(Long idx, String purpose);

    Optional<List<UserBadge>> findUserBadgeByUser_UserIndexAndBadge_BadgePurposeAndUserbadgeIsVisibleIsTrue(Long userIdx, String purpose);

    Optional<UserBadge> findUserBadgeByUserAndBadge(Users user, Badge badge);
}
