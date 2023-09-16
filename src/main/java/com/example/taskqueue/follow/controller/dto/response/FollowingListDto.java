package com.example.taskqueue.follow.controller.dto.response;

import com.example.taskqueue.common.dto.SimpleUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowingListDto {

    @ApiModelProperty(value = "팔로잉 유저 리스트")
    @JsonProperty("followingList")
    private List<FollowingUserDto> followingUserDtos = new ArrayList<>();
}
