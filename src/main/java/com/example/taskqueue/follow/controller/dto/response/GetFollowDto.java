package com.example.taskqueue.follow.controller.dto.response;

import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetFollowDto {

    @ApiModelProperty(value = "팔로우 아이디 값", example = "1")
    private Long id;

    @ApiModelProperty(value = "팔로우하는 유저 정보")
    @JsonProperty("user")
    private SimpleUserDto simpleUserDto;

    @ApiModelProperty(value = "팔로잉 할 유저 아이디 값", example = "2")
    private Long followUserId;

}
