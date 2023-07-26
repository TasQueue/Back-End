package com.example.taskqueue.follow.controller;

import com.example.taskqueue.follow.entity.Follow;
import com.example.taskqueue.follow.service.FollowService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Follow", description = "Follow 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {

    private FollowService followService;

    @ApiResponse()
    @ResponseStatus()
    @GetMapping("/info/{memberId}")
    public Response<?> searchFollowInfo(@PathVariable("memberId") Long memberId) {
        List<Follow> following = followService.findFollowing(memberId);
        List<Follow> follower = followService.findFollower(memberId);

    }

    @ApiResponse()
    @ResponseStatus()
    @PostMapping("/request/{userId}")
    public Response<?> requestFollow(
            @RequestParam("userId") Long userId,
            @RequestParam("followUserId") Long followUserId) {
        followService.requestFollow(userId, followUserId);
    }

    @ApiResponse()
    @ResponseStatus()
    @PostMapping("/approve/{userId}")
    public Response<?> approveFollow() {
        followService.acceptFollow();
    }

    @ApiResponse()
    @ResponseStatus()
    @DeleteMapping("/reject/{userId}")
    public Response<?> deleteFollow(@RequestParam()) {
        followService.deleteFollow();
    }

    @ApiResponse()
    @ResponseStatus()
    @GetMapping("/search/{userId}")
    public Response<?> searchFollow() {
        followService.
    }


}
