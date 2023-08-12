package com.example.taskqueue.follow.controller;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.follow.controller.dto.response.GetFollowDto;
import com.example.taskqueue.follow.controller.dto.response.FollowerListDto;
import com.example.taskqueue.follow.controller.dto.response.FollowingListDto;
import com.example.taskqueue.follow.controller.dto.response.RequestFollowListDto;
import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.entity.state.FollowState;
import com.example.taskqueue.follow.service.FollowService;
import com.example.taskqueue.task.controller.dto.response.GetTaskDto;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "Follow API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class FollowController {

    @Value("${domain.name}")
    private String host;

    private final FollowService followService;
    private final UserService userService;

    @ApiOperation(
            value = "팔로잉 리스트 조회하기",
            notes = "내가 팔로잉 하는 유저 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = FollowingListDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping("/follows/you")
    public ResponseEntity<FollowingListDto> getFollowingInfo(
            @ApiIgnore @CurrentUser User user) {

        List<Long> following = followService.findFollowing(user.getId());
        List<SimpleUserDto> dtoList = new ArrayList<>();

        for (Long followingId : following) {
            SimpleUserDto userDto = new SimpleUserDto(userService.findById(followingId));
            dtoList.add(userDto);
        }

        return ResponseEntity.ok(new FollowingListDto(dtoList));
    }

    @ApiOperation(
            value = "팔로워 리스트 조회하기",
            notes = "나를 팔로우 하는 유저 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = FollowerListDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping("/follows/me")
    public ResponseEntity<FollowerListDto> getFollowerInfo(
            @ApiIgnore @CurrentUser User user) {

        List<Long> follower = followService.findFollower(user.getId());
        List<SimpleUserDto> dtoList = new ArrayList<>();

        for (Long followerId : follower) {
            SimpleUserDto userDto = new SimpleUserDto(userService.findById(followerId));
            dtoList.add(userDto);
        }

        return ResponseEntity.ok(new FollowerListDto(dtoList));

    }

    @ApiOperation(
            value = "팔로우 정보 조회하기(단건)",
            notes = "팔로우 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = GetFollowDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping ("/follows/{followId}")
    public ResponseEntity<GetFollowDto> getFollow(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        return ResponseEntity.ok(new GetFollowDto(findFollow.getId(), new SimpleUserDto(findFollow.getUser()), findFollow.getFollowUserId()));
    }

    @ApiOperation(
            value = "팔로우 요청하기(생성)",
            notes = "팔로우를 REQUEST 상태로 생성한다."
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @PostMapping("/follows/request/{followUserId}")
    public ResponseEntity<Void> requestFollow(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("followUserId") Long followUserId) {

        Follow follow = Follow.builder()
                .user(user)
                .followUserId(followUserId)
                .followState(FollowState.REQUEST)
                .build();

        Long followId = followService.saveFollow(follow);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/follows/{followId}")
                .build(followId);

        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(
            value = "팔로워 요청 리스트 조회하기",
            notes = "나에게 팔로우 요청한 유저 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = RequestFollowListDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping("/follows/request/me")
    public ResponseEntity<RequestFollowListDto> getRequestFollowInfo(
            @ApiIgnore @CurrentUser User user) {

        System.out.println(" ==== ");
        List<Long> requestFollow = followService.findRequestFollow(user.getId());
        System.out.println("requestFollow = " + requestFollow);
        List<SimpleUserDto> dtoList = new ArrayList<>();

        for (Long requestFollowId : requestFollow) {
            SimpleUserDto userDto = new SimpleUserDto(userService.findById(requestFollowId));
            dtoList.add(userDto);
        }

        return ResponseEntity.ok(new RequestFollowListDto(dtoList));

    }

    @ApiOperation(
            value = "팔로우 수락하기",
            notes = "팔로우 정보를 ACCEPT 로 바꾸어 수락한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @PutMapping ("/follows/{followId}/approve")
    public ResponseEntity<Void> approveFollow(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        followService.acceptFollow(findFollow);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "팔로우 거절하기(삭제)",
            notes = "팔로우 정보를 삭제한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "DELETED"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @DeleteMapping("/follows/{followId}/reject")
    public ResponseEntity<?> deleteFollow(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        followService.deleteFollow(findFollow);
        return ResponseEntity.noContent().build();
    }

}

