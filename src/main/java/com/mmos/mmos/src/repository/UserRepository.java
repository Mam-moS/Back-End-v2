package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUserByUserEmail(String email);

    Optional<Users> findUserByUserId(String id);

    boolean existsUserByUserId(String id);

    Optional<Users> findUserByUserIndexAndUserPassword(Long idx, String pwd);
}
