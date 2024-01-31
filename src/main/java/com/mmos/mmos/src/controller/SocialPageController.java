package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.BusinessLogicException;
import com.mmos.mmos.config.exception.NotAuthorizedAccessException;
import com.mmos.mmos.src.domain.dto.request.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.response.social.FriendPlannerResponseDto;
import com.mmos.mmos.src.domain.dto.response.social.GetFriendDto;
import com.mmos.mmos.src.domain.dto.response.social.SearchFriendDto;
import com.mmos.mmos.src.domain.dto.response.social.SocialPageResponseDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/social")
@RequiredArgsConstructor
public class SocialPageController extends BaseController {

    private final FriendService friendService;
    private final UserService userService;
    private final UserBadgeService userBadgeService;
    private final PlannerService plannerService;
    private final CalendarService calendarService;

    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal Users tokenUser) {
        try {
            Long userIdx = tokenUser.getUserIndex();
            // 기본 쿼리
            userBadgeService.saveUserBadge(userIdx);
            friendService.friendWithMe(userIdx);

            // 로직
            List<Friend> friends = friendService.getFriends(userIdx, 1);
            List<Users> top3 = friendService.getTop3Friends(userIdx, 1);
            Integer receivedFriendRequestsNum = friendService.getFriends(userIdx, 3).size();

            return sendResponseHttpByJson(SUCCESS, "소셜 페이지 로드 성공", new SocialPageResponseDto(tokenUser.getUserIndex(), friends, top3, receivedFriendRequestsNum));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 검색
    @GetMapping("/{friendId}")
    public ResponseEntity<ResponseApiMessage> searchFriend(@AuthenticationPrincipal Users tokenUser, @PathVariable String friendId) {
        try {
            if(tokenUser.getUserId().equals(friendId))
                throw new BusinessLogicException(CANNOT_FRIEND_WITH_ME);

            return sendResponseHttpByJson(SUCCESS, "친구 찾기 성공", new SearchFriendDto(userService.findUserById(friendId)));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 보내기
    @PostMapping("/request/{friendId}")
    public ResponseEntity<ResponseApiMessage> sendFriendRequest(@AuthenticationPrincipal Users tokenUser, @PathVariable String friendId) {
        try {
            Friend request = friendService.sendFriendRequest(tokenUser.getUserIndex(), friendId);

            return sendResponseHttpByJson(SUCCESS, "친구 요청 보내기 성공", request);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 수락
    @PatchMapping("/request/{friendIdx}")
    public ResponseEntity<ResponseApiMessage> acceptFriendRequest(@AuthenticationPrincipal Users tokenUser, @PathVariable Long friendIdx) {
        try {
            Friend response = friendService.acceptFriendRequest(tokenUser.getUserIndex(), friendIdx);
            List<Friend> myFriends = response.getUser().getUserFriends();

            return sendResponseHttpByJson(SUCCESS, "친구 요청 수락 성공", myFriends);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 요청 거부/취소/친구 삭제
    @DeleteMapping("/request/{friendIdx}")
    public ResponseEntity<ResponseApiMessage> deleteFriend(@AuthenticationPrincipal Users tokenUser, @PathVariable Long friendIdx) {
        try {
            Integer friendStatus = friendService.deleteFriendRequest(tokenUser.getUserIndex(), friendIdx);
            List<Friend> myFriends = friendService.getFriends(tokenUser.getUserIndex(), friendStatus);

            return sendResponseHttpByJson(SUCCESS, "친구 요청 삭제", myFriends);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 받은 친구 요청 목록
    @GetMapping("/receive")
    public ResponseEntity<ResponseApiMessage> getReceivedFriendRequests(@AuthenticationPrincipal Users tokenUser) {
        try {
            List<Friend> requestList = friendService.getFriends(tokenUser.getUserIndex(), 3);

            List<GetFriendDto> result = new ArrayList<>();
            for (Friend friend : requestList) {
                result.add(new GetFriendDto(friend));
            }

            return sendResponseHttpByJson(SUCCESS, "받은 친구 요청 목록 조회 성공", result);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 보낸 친구 요청 목록
    @GetMapping("/send")
    public ResponseEntity<ResponseApiMessage> getSentFriendRequests(@AuthenticationPrincipal Users tokenUser) {
        try {
            List<Friend> requestList = friendService.getFriends(tokenUser.getUserIndex(), 2);

            List<GetFriendDto> result = new ArrayList<>();
            for (Friend friend : requestList) {
                result.add(new GetFriendDto(friend));
            }

            return sendResponseHttpByJson(SUCCESS, "보낸 친구 요청 목록 조회 성공", result);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 상단 고정
    @PatchMapping("/fix/{friendIdx}")
    public ResponseEntity<ResponseApiMessage> updateIsFixed(@AuthenticationPrincipal Users tokenUser, @PathVariable Long friendIdx) {
        try {
            friendService.updateIsFixed(tokenUser.getUserIndex(), friendIdx);
            List<Friend> myFriends = friendService.getFriends(tokenUser.getUserIndex(), 1);

            return sendResponseHttpByJson(SUCCESS, "친구 상단 고정/해제 성공", myFriends);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 친구 플래너 확인
    @GetMapping("/friendInfo/{friendIndex}")
    public ResponseEntity<ResponseApiMessage> getFriendPlanner(@AuthenticationPrincipal Users tokenUser, @PathVariable Long friendIndex) {
        try {
            Friend friend = friendService.getFriend(friendIndex);
            Users friendUser = friend.getFriend();
            if (!friendUser.getIsPlannerVisible())
                throw new NotAuthorizedAccessException(FORBIDDEN_PLANNER);
            if (!tokenUser.getUserIndex().equals(friendUser.getUserIndex())) {
                boolean isExist = false;
                for (Friend myAllFriend : friendService.getFriends(tokenUser.getUserIndex(), 1)) {
                    if (!myAllFriend.getFriend().equals(friendUser)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    throw new BaseException(EMPTY_FRIEND);
                }
            }

            Badge tier = userBadgeService.getRepresentBadges(friendUser.getUserIndex(), "tier").get(0).getBadge();

            List<Badge> badges = new ArrayList<>();
            List<UserBadge> userBadges = userBadgeService.getRepresentBadges(tokenUser.getUserIndex(), "badge");
            for (UserBadge userBadge : userBadges) {
                badges.add(userBadge.getBadge());
            }

            Badge pfp = userBadgeService.getRepresentBadges(friendUser.getUserIndex(), "pfp").get(0).getBadge();

            Planner planner = plannerService.getPlannerByCalendarAndDate(calendarService.getCalendar(
                            friendUser.getUserIndex(),
                            new CalendarGetRequestDto(LocalDate.now().getMonthValue(),
                                    LocalDate.now().getYear())).getIdx(),
                    LocalDate.now());

            return sendResponseHttpByJson(SUCCESS, "친구 플래너 조회 성공", new FriendPlannerResponseDto(friendUser, tier, badges, pfp, planner));
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

}
