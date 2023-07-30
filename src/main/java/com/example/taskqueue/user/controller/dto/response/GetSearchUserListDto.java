package com.example.taskqueue.user.controller.dto.response;

import com.example.taskqueue.common.dto.SimpleUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetSearchUserListDto {

    @Schema(description = "검색으로 조회된 유저 리스트")
    @JsonProperty("searchList")
    private List<SimpleUserDto> simpleUserDtos = new ArrayList<>();

}
