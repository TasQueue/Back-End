package com.example.taskqueue.user.controller.dto.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    @ApiModelProperty(value = "변경할 유저 이름(변경 안하더라도 기존 이름 필요)", example = "홍길동")
    @NotBlank(message = "유저 이름은 필수 값입니다.")
    private String name;

    @ApiModelProperty(value = "변경할 한 줄 소개", example = "안녕하세요. 반갑습니다.")
    private String intro;

    @ApiModelProperty(value = "잔디 색깔", example = "ABC123")
    @NotBlank(message = "잔디 색깔은 필수 값입니다.")
    private String color;

}
