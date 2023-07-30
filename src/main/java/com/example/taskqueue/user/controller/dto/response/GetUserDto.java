package com.example.taskqueue.user.controller.dto.response;

import com.example.taskqueue.user.entity.state.CatState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserDto {

    @Schema(description = "유저 이름정보", example = "전용수")
    private String name;

    @Schema(description = "유저 한 줄 소개", example = "안녕하세요")
    private String intro;

    @Schema(description = "유저 고양이 상태", example = "ONE | TWO | THREE | FOUR")
    private CatState catState;

}
