package com.example.taskqueue.common.dto;

import com.example.taskqueue.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleUserDto {

    @Schema(description = "유저 아이디 값", example = "1")
    private Long id;

    @Schema(description = "유저 카카오 이메일", example = "kakao@gmail.com")
    private String email;

    @Schema(description = "유저 이름", example = "전용수")
    private String name;

    public SimpleUserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
