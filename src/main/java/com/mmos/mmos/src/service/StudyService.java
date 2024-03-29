package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.dto.request.StudySaveRequestDto;
import com.mmos.mmos.src.domain.dto.request.StudyUpdateRequestDto;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.Users;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.PostRepository;
import com.mmos.mmos.src.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_STUDY;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final PostRepository postRepository;

    public Study findStudyByIdx(Long studyIdx) throws BaseException {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_STUDY));
    }
    
    // 스터디 업데이트 (이름, 메모, 완수여부 설정)
    @Transactional
    public Study updateStudy(Long studyIdx, StudyUpdateRequestDto requestDto) throws BaseException {
        try {
            Study study = findStudyByIdx(studyIdx);

            if(!requestDto.getNewName().isEmpty())
                study.updateStudyName(requestDto.getNewName());
            if(!requestDto.getNewMemo().isEmpty())
                study.updateStudyMemo(requestDto.getNewMemo());
            if(requestDto.getMemberLimit() != null) {
                study.updateMemberNum(requestDto.getMemberLimit());
            }
            if(requestDto.getIsComplete()) {
                study.updateStudyIsComplete();
                postRepository.deleteAll(study.getStudyPosts());
            }

            return study;
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Users> getStudyAppliersOrInvitee(Long studyIdx, Integer status) throws BaseException {
        try {
            Study study = findStudyByIdx(studyIdx);

            // UserStudy list에서 지원자 선별
            List<Users> appliersDto = new ArrayList<>();
            for (UserStudy userStudy : study.getStudyUserstudies()){
                if(userStudy.getUserStudyMemberStatus().equals(status)) {
                    appliersDto.add(userStudy.getUser());
                }
            }

            return appliersDto;
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Study getStudy(Long studyIdx) throws BaseException {
        try {
            System.out.println(studyIdx);
            return findStudyByIdx(studyIdx);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteStudy(Study study) throws BaseException {
        try {
            studyRepository.delete(study);
        }  catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Study saveStudy(StudySaveRequestDto requestDto) throws BaseException {
        try {
            return studyRepository.save(new Study(requestDto));
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
