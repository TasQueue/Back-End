package com.example.taskqueue.user.controller.dto.response;

import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GetUserDto {

    @ApiModelProperty(value = "유저 아이디 정보", example = "1")
    private Long id;

    @ApiModelProperty(value = "유저 이름정보", example = "전용수")
    private String name;

    @ApiModelProperty(value = "유저 한 줄 소개", example = "안녕하세요")
    private String intro;

    @ApiModelProperty(value = "유저 고양이 상태", example = "ONE | TWO | THREE | FOUR")
    private CatState catState;

    @ApiModelProperty(value = "유저 테마 색상", example = "ABC123")
    private String color;

    public GetUserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.intro = user.getIntro();
        this.catState = user.getCatState();
        this.color = user.getThemeColor();
    }

}
