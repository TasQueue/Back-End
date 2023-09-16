package com.example.taskqueue.follow.controller.dto.response;

import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.user.entity.state.CatState;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowingUserDto {

    @ApiModelProperty(value = "팔로우하는 유저 정보")
    @JsonProperty("user")
    private SimpleUserDto simpleUserDto;

    @ApiModelProperty(value = "팔로잉 유저 고양이 상태")
    private CatState catState;

    @ApiModelProperty(value = "팔로우하는 유저 테마색깔")
    private String themeColor;
}
