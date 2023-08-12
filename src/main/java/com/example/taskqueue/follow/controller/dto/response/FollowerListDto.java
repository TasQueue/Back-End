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
public class FollowerListDto {

    @ApiModelProperty(value = "팔로워 유저 리스트")
    @JsonProperty("followerList")
    private List<SimpleUserDto> simpleUserDtos = new ArrayList<>();
}
