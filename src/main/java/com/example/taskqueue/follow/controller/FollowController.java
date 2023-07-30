package com.example.taskqueue.follow.controller;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.follow.controller.dto.response.GetFollowDto;
import com.example.taskqueue.follow.controller.dto.response.FollowerListDto;
import com.example.taskqueue.follow.controller.dto.response.FollowingListDto;
import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.service.FollowService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @ApiResponse()
    @ResponseStatus()
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

    @ApiResponse()
    @ResponseStatus()
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

    @ApiResponse()
    @ResponseStatus()
    @GetMapping ("/follows/{followId}")
    public ResponseEntity<GetFollowDto> getFollow(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        return ResponseEntity.ok(new GetFollowDto(findFollow.getId(), findFollow.getUser(), findFollow.getFollowUserId()));
    }

    @ApiResponse()
    @ResponseStatus()
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

    @ApiResponse()
    @ResponseStatus()
    @PutMapping ("/follows/{followId}/approve")
    public ResponseEntity<Void> approveFollow(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        followService.acceptFollow(findFollow);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse()
    @ResponseStatus()
    @DeleteMapping("/follows/{followId}/reject")
    public ResponseEntity<?> deleteFollow(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("followId") Long followId) {

        Follow findFollow = followService.findById(followId);
        followService.deleteFollow(findFollow);
        return ResponseEntity.noContent().build();
    }

}
