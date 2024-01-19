package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.src.domain.dto.request.LoginRequestDto;
import com.mmos.mmos.src.domain.dto.request.UpdateIdRequestDto;
import com.mmos.mmos.src.domain.dto.request.UpdatePwdRequestDto;
import com.mmos.mmos.src.domain.dto.request.UserDeleteRequestDto;
import com.mmos.mmos.src.domain.dto.response.MyPageResponseDto;
import com.mmos.mmos.src.domain.entity.Major;
import com.mmos.mmos.src.domain.entity.Users;
import com.mmos.mmos.src.domain.entity.UserBadge;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.mmos.mmos.config.HttpResponseStatus.*;
import static com.mmos.mmos.utils.SHA256.encrypt;

@RestController
@RequestMapping("/api/v1/info")
@RequiredArgsConstructor
public class MyPageController extends BaseController {

    private final UserService userService;
    private final UserBadgeService userBadgeService;
    private final AuthService authService;
    private final FriendService friendService;
    private final MajorService majorService;

    // 페이지 로드
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal Users tokenUser) {
        try {
            // 구현 필요
            Users user = userService.getUser(tokenUser.getUserIndex());

            return sendResponseHttpByJson(SUCCESS, "페이지 로드 성공",
                    new MyPageResponseDto(user,
                            userBadgeService.getRepresentBadges(user.getUserIndex(), "pfp").get(0).getBadge().getBadgeIcon()));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /***
     * UserBadge prevPfp = userBadgeService.getRepresentBadges(userIdx, "pfp") => null
     */
    // 프사 변경
    @PatchMapping("/pfp/{pfpIdx}")
    public ResponseEntity<ResponseApiMessage> updatePfp(@AuthenticationPrincipal Users tokenUser, @PathVariable Long pfpIdx) {
        try {
            // 기존 프사 찾기
            Users user = userService.getUser(tokenUser.getUserIndex());
            UserBadge prevPfp = userBadgeService.getRepresentBadges(tokenUser.getUserIndex(), "pfp").get(0);
            // 새 프사 찾기
            UserBadge newPfp = null;
            boolean isExist = false;
            for (UserBadge userUserbadge : user.getUserUserbadges()) {
                if(userUserbadge.getBadge().getBadgeIndex().equals(pfpIdx)) {
                    newPfp = userUserbadge;
                    isExist = true;
                }
            } if(!isExist)
                throw new BaseException(EMPTY_USERBADGE);

            // 이미 대표 프사인 뱃지와 선택한 뱃지가 같다면 변경 성공했다는 문구는 뜨지만 사실 업데이트 되진 않았음
            if(prevPfp.equals(newPfp))
                return sendResponseHttpByJson(SUCCESS, "프로필 사진 변경 성공", newPfp);

            userBadgeService.updatePfp(prevPfp, false);
            userBadgeService.updatePfp(newPfp, true);

            return sendResponseHttpByJson(SUCCESS, "프로필 사진 변경 성공", newPfp);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 아이디 변경
    @PatchMapping("/id")
    public ResponseEntity<ResponseApiMessage> updateId(@AuthenticationPrincipal Users tokenUser, @RequestBody UpdateIdRequestDto requestDto) {
        try {
            String id = requestDto.getId();
            String pwd = requestDto.getPwd();

            // 입력 아이디 중복 처리
            if(userService.isExistById(id))
                throw new DuplicateRequestException(UPDATE_DUPLICATE_ID);

            Users user = userService.findUserByIdAndPwd(tokenUser.getUserIndex(), encrypt(pwd));
            // 아이디 변경
            userService.updateId(user, id);

            return sendResponseHttpByJson(SUCCESS, "아이디 변경 성공", authService.authenticate(new LoginRequestDto(id, pwd)));
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 학과 변경
    @PatchMapping("/major/{majorIndex}")
    public ResponseEntity<ResponseApiMessage> updateMajor(@AuthenticationPrincipal Users tokenUser, @PathVariable Long majorIndex) {
        try {
            Users user = userService.getUser(tokenUser.getUserIndex());
            // 기존 전공을 가져오기
            if(Objects.equals(user.getMajor().getMajorIndex(), majorIndex))
                // 변경 성공했다는 문구는 뜨지만 사실 업데이트 되진 않았음
                return sendResponseHttpByJson(SUCCESS, "전공 변경 성공", null);
            // 전공 변경
            Major newMajor = majorService.getMajor(majorIndex);
            userService.updateMajor(user, newMajor);

            return sendResponseHttpByJson(SUCCESS, "전공 변경 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 비밀번호 변경
    @PatchMapping("/pwd")
    public ResponseEntity<ResponseApiMessage> updatePwd(@AuthenticationPrincipal Users tokenUser, @RequestBody UpdatePwdRequestDto requestDto) {
        try {
            Users user = userService.getUser(tokenUser.getUserIndex());
            String prevPwd = encrypt(requestDto.getPrevPwd());
            String newPwd = encrypt(requestDto.getNewPwd());

            if(requestDto.getPrevPwd().isEmpty()
                    || requestDto.getNewPwd().isEmpty()
                    || requestDto.getCheckPwd().isEmpty())
                throw new BaseException(UPDATE_USER_EMPTY_PWD);
            if(!user.getPassword().equals(prevPwd))
                throw new BaseException(UPDATE_USER_DIFF_PREVPWD);
            if(!requestDto.getNewPwd().equals(requestDto.getCheckPwd()))
                throw new BaseException(UPDATE_USER_DIFF_NEWPWD);

            // 비밀번호 변경
            userService.updatePwd(user, newPwd);

            return sendResponseHttpByJson(SUCCESS, "비밀번호 변경 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 플래너 공개 설정
    @PatchMapping("/planner")
    public ResponseEntity<ResponseApiMessage> updateIsPlannerVisible(@AuthenticationPrincipal Users tokenUser) {
        try {
            Users user = userService.getUser(tokenUser.getUserIndex());
            userService.updateIsVisible(user);

            return sendResponseHttpByJson(SUCCESS, "플래너 공개 여부 변경 성공", user.getIsPlannerVisible());
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 회원 탈퇴
    @DeleteMapping("")
    public ResponseEntity<ResponseApiMessage> deleteUser(@AuthenticationPrincipal Users tokenUser, @RequestBody UserDeleteRequestDto requestDto) {
        try {
            Users user = userService.getUser(tokenUser.getUserIndex());

            if(!user.getUserPassword().equals(encrypt(requestDto.getPwd())))
                throw new BaseException(UPDATE_USER_DIFF_PREVPWD);

            friendService.deleteFriends(user);
            userService.deleteUser(user);

            return sendResponseHttpByJson(SUCCESS, "회원 탈퇴 성공", null);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
