package com.example.taskqueue.follow.controller;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.follow.controller.dto.response.GetFollowDto;
import com.example.taskqueue.follow.controller.dto.response.FollowerListDto;
import com.example.taskqueue.follow.controller.dto.response.FollowingListDto;
import com.example.taskqueue.follow.controller.dto.response.RequestFollowListDto;
import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.service.FollowService;
import com.example.taskqueue.task.controller.dto.response.GetTaskDto;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Follow", description = "Follow 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class FollowController {

    @Value("${domain.name}")
    private String host;

    private FollowService followService;
    private UserService userService;

    @Operation(summary = "팔로잉 리스트 조회하기", description = "내가 팔로잉 하는 유저 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowingListDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/follows/you")
    public ResponseEntity<FollowingListDto> getFollowingInfo(
            @Parameter(hidden = true) @CurrentUser User user) {

        List<Long> following = followService.findFollowing(user.getId());
        List<SimpleUserDto> dtoList = new ArrayList<>();

        for (Long followingId : following) {
            SimpleUserDto userDto = new SimpleUserDto(userService.findById(followingId));
            dtoList.add(userDto);
        }

        return ResponseEntity.ok(new FollowingListDto(dtoList));
    }

    @Operation(summary = "팔로워 리스트 조회하기", description = "나를 팔로우 하는 유저 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowerListDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/follows/me")
    public ResponseEntity<FollowerListDto> getFollowerInfo(
            @Parameter(hidden = true) @CurrentUser User user) {

        List<Long> follower = followService.findFollower(user.getId());
        List<SimpleUserDto> dtoList = new ArrayList<>();

        for (Long followerId : follower) {
            SimpleUserDto userDto = new SimpleUserDto(userService.findById(followerId));
            dtoList.add(userDto);
        }

        return ResponseEntity.ok(new FollowerListDto(dtoList));

    }

    @Operation(summary = "팔로우 정보 조회하기(단건)", description = "팔로우 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetFollowDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping ("/follows/{followId}")
    public ResponseEntity<GetFollowDto> getFollow(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        return ResponseEntity.ok(new GetFollowDto(findFollow.getId(), findFollow.getUser(), findFollow.getFollowUserId()));
    }

    @Operation(summary = "팔로우 요청하기", description = "팔로우를 REQUEST 상태로 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetFollowDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/follows/request/{followUserId}")
    public ResponseEntity<Void> requestFollow(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("followUserId") Long followUserId) {

        Long followId = followService.requestFollow(user.getId(), followUserId);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/follows/{followId}")
                .build(followId);

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "팔로워 요청 리스트 조회하기", description = "나에게 팔로우 요청한 유저 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestFollowListDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/request/me")
    public ResponseEntity<RequestFollowListDto> getRequestFollowInfo(
            @Parameter(hidden = true) @CurrentUser User user) {

        List<Long> requestFollow = followService.findRequestFollow(user.getId());
        List<SimpleUserDto> dtoList = new ArrayList<>();

        for (Long requestFollowId : requestFollow) {
            SimpleUserDto userDto = new SimpleUserDto(userService.findById(requestFollowId));
            dtoList.add(userDto);
        }

        return ResponseEntity.ok(new RequestFollowListDto(dtoList));

    }

    @Operation(summary = "팔로우 수락하기", description = "팔로우 정보를 ACCEPT 로 바꾸어 수락한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT",
                    content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping ("/follows/{followId}/approve")
    public ResponseEntity<Void> approveFollow(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        followService.acceptFollow(findFollow);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "팔로우 거절하기", description = "팔로우 정보를 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "DELETED",
                    content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/follows/{followId}/reject")
    public ResponseEntity<?> deleteFollow(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        followService.deleteFollow(findFollow);
        return ResponseEntity.noContent().build();
    }

}
