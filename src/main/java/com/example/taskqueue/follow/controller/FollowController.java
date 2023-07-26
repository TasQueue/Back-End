package com.example.taskqueue.follow.controller;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.follow.controller.dto.response.GetFollowingDto;
import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.service.FollowService;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Follow", description = "Follow 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class FollowController {

    private FollowService followService;
    private UserService userService;

    @ApiResponse()
    @ResponseStatus()
    @GetMapping("/api/follows/you")
    public ResponseEntity<?> getFollowingInfo(
            @Parameter(hidden = true) @CurrentUser User user) {
        List<Long> following = followService.findFollowing(user.getId());
        List<Long> userId = null;
        for (Long followingId : following) {
            userId.add(userService.findById(followingId).getId());
        }
        return ResponseEntity.ok(new GetFollowingDto());
    }
//
//    @GetMapping("/api/follows/me")
//    public ResponseEntity<?> getFollwerInfo() {
//
//    }
//
//    @ApiResponse()
//    @ResponseStatus()
//    @PostMapping("/api/follows/request/{userId}")
//    public Response<?> requestFollow(
//            @RequestParam("userId") Long userId,
//            @RequestParam("followUserId") Long followUserId) {
//        followService.requestFollow(userId, followUserId);
//    }
//
//    @ApiResponse()
//    @ResponseStatus()
//    @PostMapping("/api/follows/{followId}/approve")
//    public Response<?> approveFollow() {
//        followService.acceptFollow();
//    }
//
//    @ApiResponse()
//    @ResponseStatus()
//    @DeleteMapping("/api/follows/request/{userId}")
//    public Response<?> deleteFollow(@RequestParam()) {
//        followService.deleteFollow();
//    }
//
//    @ApiResponse()
//    @ResponseStatus()
//    @GetMapping("/search/{userId}")
//    public Response<?> searchFollow() {
//        followService.
//    }


}
