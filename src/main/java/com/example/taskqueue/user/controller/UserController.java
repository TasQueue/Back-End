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
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "User API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final DayOfWeekService dayOfWeekService;

    @ApiOperation(
            value = "유저 본인 정보 수정하기",
            notes = "로그인 중인 유저의 기본 정보를 수정한다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @PutMapping(value = "/users")
    public ResponseEntity<Void> updateUserInfo(
            @ApiIgnore @CurrentUser User user,
            @RequestBody @Valid UserUpdateDto userUpdateDto
    ) {
        userService.changeUser(
                user.getId(),
                userUpdateDto.getName(),
                userUpdateDto.getIntro(),
                userUpdateDto.getColor()
        );
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(
            value = "특정 유저 정보 조회하기",
            notes = "아이디 값으로 유저의 기본 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = GetUserDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<GetUserDto> getUserInfo(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("userId") Long userId
    ) {

        User findUser = userService.findById(userId);

        if(!findUser.getDailyUpdate())
        {
            userService.updateDailyState(findUser, LocalDate.now());
        }

        int state = taskService.getStateOfCat(userId);
        CatState catState;

        switch (state) {
            case 0:
                catState = CatState.FOUR;
                break;
            case 1:
                catState = CatState.THREE;
                break;
            case 2:
                catState = CatState.TWO;
                break;
            default:
                catState = CatState.ONE;
                break;
        }

        findUser.updateCatState(catState);
        return ResponseEntity.ok(new GetUserDto(findUser));
    }


    @ApiOperation(
            value = "유저 본인 계정 삭제학기",
            notes = "회원 탈퇴한다. 계정은 영구 삭제된다."
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/users/delete")
    public ResponseEntity<Void> deleteUserInfo(
            @ApiIgnore @CurrentUser User user
    ) {
        userService.deleteUser(user);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(
            value = "검색한 유저 정보 리스트 조회",
            notes = "유저 이름 완전 일치해야 조회됩니다."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = GetSearchUserListDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping(value = "/users/search/{userName}")
    public ResponseEntity<GetSearchUserListDto> getUserInfoBySearch(
            @ApiIgnore @CurrentUser User user,
            @PathVariable("userName") String userName
    ) {
        List<User> findList = userService.findByName(userName);
        List<SimpleUserDto> dtoList = findList.stream().map(SimpleUserDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(new GetSearchUserListDto(dtoList));
    }


    @ApiOperation(
            value = "자신의 로그인 정보 조회",
            notes = "자신의 로그인 정보 조회."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = GetUserDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    @GetMapping(value = "/users/my-info")
    public ResponseEntity<GetUserDto> getMyUserInfo(
            @ApiIgnore @CurrentUser User user
    ) {
        return ResponseEntity.ok(new GetUserDto(user));
    }



}
