package com.example.taskqueue.user.controller;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.user.controller.dto.UserUpdateDto;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 수정하기", description = "유저 정보를 수정한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/api/users")
    public ResponseEntity<Void> updateUserInfo(
            @Parameter(hidden = true) @CurrentUser User user,
            @ModelAttribute UserUpdateDto userUpdateDto
    ) {
        user.updateThemeColor(userUpdateDto.getColor());
        user.updateName(userUpdateDto.getName());
        user.updateIntro(userUpdateDto.getIntro());
        return ResponseEntity.noContent().build();
    }



}
