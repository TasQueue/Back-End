package com.example.taskqueue.user.controller.dto.response;

import com.example.taskqueue.common.dto.SimpleUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetSearchUserListDto {

    @ApiModelProperty(value = "검색으로 조회된 유저 리스트")
    @JsonProperty("searchList")
    private List<SimpleUserDto> simpleUserDtos = new ArrayList<>();

}
