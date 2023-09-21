package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.user.UserNicknameUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserPwdUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserResponseDto;
import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Major;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.MajorRepository;
import com.mmos.mmos.src.repository.UniversityRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX" + userIdx));
    }

    public University findUniversityByIdx(Long universityIdx) {
        return universityRepository.findById(universityIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대학입니다 UNIVERSITY_INDEX=" + universityIdx));
    }

    public Major findMajorByIdx(Long majorIdx) {
        return majorRepository.findById(majorIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다. UNIVERSITY_INDEX=" + majorIdx));
    }

    // 유저 생성
    @Transactional
    public UserResponseDto saveUser(UserSaveRequestDto requestDto) {
        University university = findUniversityByIdx(requestDto.getUniversityIdx());
        Major major = findMajorByIdx(requestDto.getMajorIdx());

        // Validation: 생성하려는 유저의 정보로 이미 가입된 회원이 있는지 확인 (학교 & 학번, 이메일)
        if(userRepository.findUserByUserEmail(requestDto.getEmail()).isPresent())
            return null;
        if(userRepository.findUserByUserStudentIdAndUniversity(requestDto.getStudentId(), university).isPresent()) {
            System.out.println("aa");
            return null;
        }

        User user = new User(requestDto, university, major);


        userRepository.save(user);

        // 대학, 전공 역 FK 설정
        university.addUser(user);
        major.addUser(user);

        return new UserResponseDto(user);
    }

    // 비밀번호 변경
    @Transactional
    public UserResponseDto updatePwd(UserPwdUpdateDto userPwdUpdateDto, Long userIdx) {
        // 유저 정보 찾기
        User user = findUserByIdx(userIdx);

        // 사용자가 입력한 이전 비밀번호와 DB에 저장된 비밀번호와 같은지 확인
        if(user.getUserPassword().equals(userPwdUpdateDto.getPrevPwd()))
            return null;

        // 유저 비밀번호 변경
        user.updatePwd(userPwdUpdateDto.getNewPwd());

        return new UserResponseDto(user);
    }

    // 닉네임 변경: 이전 닉네임과 같은지 확인하지 않은 이유 -> 이전 닉네임과 비교하려면 DB와 접촉을 한번 더 해야하므로 Regex만 거치면 그냥 받는대로 UPDATE
    @Transactional
    public UserResponseDto updateNickname(UserNicknameUpdateDto userNicknameUpdateDto, Long userIdx) {
        // 유저 정보 찾기
        User user = findUserByIdx(userIdx);
        // 유저 비밀번호 변경
        user.updateNickname(userNicknameUpdateDto.getNewNickname());

        return new UserResponseDto(user);
    }

    // 프로필 이미지 변경: 닉네임과 사유 같음
    @Transactional
    public UserResponseDto updatePfp(String pfp, Long userIdx) {
        // 유저 정보 찾기
        User user = findUserByIdx(userIdx);
        // 유저 프로필 이미지 변경
        user.updatePfp(pfp);

        return new UserResponseDto(user);
    }
}
