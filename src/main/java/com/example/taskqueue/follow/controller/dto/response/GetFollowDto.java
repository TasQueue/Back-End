package com.example.taskqueue.follow.controller.dto.response;

import com.example.taskqueue.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetFollowDto {

    @Schema(description = "팔로우 아이디 값")
    private Long id;

    @Schema(description = "팔로우하는 유저 정보")
    private User user;

    @Schema(description = "팔로잉 할 유저 아이디 값")
    private Long followUserId;

}
