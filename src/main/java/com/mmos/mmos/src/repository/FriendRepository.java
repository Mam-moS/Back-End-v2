package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Friend;
import com.mmos.mmos.src.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<List<Friend>> findFriendsByUser_UserIndexAndFriendStatus(Long userIdx, Integer status);

    Optional<Friend> findFriendByUserAndFriend(Users user, Users friend);

    List<Friend> findFriendsByFriend(Users friend);

    boolean existsFriendByUserAndFriend(Users user, Users friend);
}
