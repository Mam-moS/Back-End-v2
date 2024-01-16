package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.Users;
import com.mmos.mmos.src.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findAllByUserAndStudy(Users user, Study study);
}
