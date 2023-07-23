package com.example.taskqueue.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserUpdateDto {

    @Schema(description = "변경할 유저 이름(변경 안하더라도 기존 이름 필요)", example = "홍길동")
    @NotBlank(message = "유저 이름은 필수 값입니다.")
    private String name;

    @Schema(description = "변경할 한 줄 소개", example = "안녕하세요. 반갑습니다.")
    private String intro;

    @Schema(description = "잔디 색깔", example = "ABC123")
    @NotBlank(message = "잔디 색깔은 필수 값입니다.")
    private String color;

}
