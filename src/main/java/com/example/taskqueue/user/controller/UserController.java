package com.example.taskqueue.user.controller;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.common.dto.SimpleUserDto;
import com.example.taskqueue.exceptionhandler.ErrorResponse;
import com.example.taskqueue.task.service.DayOfWeekService;
import com.example.taskqueue.task.service.TaskService;
import com.example.taskqueue.user.controller.dto.request.UserUpdateDto;
import com.example.taskqueue.user.controller.dto.response.GetSearchUserListDto;
import com.example.taskqueue.user.controller.dto.response.GetUserDto;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
import com.example.taskqueue.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "User", description = "User 에 관련된 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final DayOfWeekService dayOfWeekService;

    @Operation(summary = "유저 본인 정보 수정하기",
            description = "유저 본인 정보(테마 색, 이름, 한줄 소개)를 수정한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/api/users")
    public ResponseEntity<Void> updateUserInfo(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody @Valid UserUpdateDto userUpdateDto
    ) {
        user.updateThemeColor(userUpdateDto.getColor());
        user.updateName(userUpdateDto.getName());
        user.updateIntro(userUpdateDto.getIntro());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "유저 정보 조회하기",
            description = "유저 정보(이름, 한줄 소개, 고양이 상태)를 조회한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/users/{userId}")
    public ResponseEntity<GetUserDto> getUserInfo(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("userId") Long userId
    ) {
        User findUser = userService.findById(userId);
        int state = taskService.getStateOfCat(userId);

        CatState catState;

        if(state == 1) {
            catState = CatState.ONE;
        } else if (state == 2) {
            catState = CatState.TWO;
        } else if (state == 3) {
            catState = CatState.THREE;
        } else {
            catState = CatState.FOUR;
        }

        findUser.updateCatState(catState);
        return ResponseEntity.ok(new GetUserDto(findUser.getName(), findUser.getIntro(), findUser.getCatState()));
    }


    @Operation(summary = "유저 삭제하기(회원 탈퇴)",
            description = "유저를 삭제(복구 불가)한다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/api/users/delete")
    public ResponseEntity<Void> deleteUserInfo(
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        return null;
    }


    @Operation(summary = "검색한 유저 정보 리스트 조회",
            description = "유저 이름 완전 일치해야 조회됩니다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetSearchUserListDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/users/search/{userName}")
    public ResponseEntity<GetSearchUserListDto> getUserInfoBySearch(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("userName") String userName
    ) {
        List<User> findList = userService.findByName(userName);
        List<SimpleUserDto> dtoList = findList.stream().map(SimpleUserDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new GetSearchUserListDto(dtoList));
    }




}
