package com.example.taskqueue.follow.controller.dto.response;

import com.example.taskqueue.common.dto.SimpleUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class FollowerListDto {

    @Schema(description = "팔로워 유저 리스트")
    @JsonProperty("followerList")
    private List<SimpleUserDto> simpleUserDtos = new ArrayList<>();
}
