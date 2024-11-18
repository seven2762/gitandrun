package com.sparta.gitandrun.user.controller;


import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.user.dto.request.SignUpReqDTO;
import com.sparta.gitandrun.user.dto.request.UserChangeNicknameReqDto;
import com.sparta.gitandrun.user.dto.request.UserChangePasswordReqDto;
import com.sparta.gitandrun.user.dto.request.UserSearchReqDto;
import com.sparta.gitandrun.user.dto.response.*;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import com.sparta.gitandrun.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserApiResDto> signup(@Valid @RequestBody SignUpReqDTO signUpReqDTO) {
        SignUpResDTO signUpResDTO = userService.signUp(signUpReqDTO);
        return ResponseEntity.ok(new UserApiResDto("회원 가입이 완료되었습니다.", 200, signUpResDTO));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResDto> softDeleteUser(@RequestParam String phone) {
        userService.softDeleteUser(phone);
        return ResponseEntity.ok(new ApiResDto("회원 소프트 딜리트 완료", 200));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResDto> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody UserChangePasswordReqDto request
    ) {
        userService.changePassword(userDetails, request);
        return ResponseEntity.ok(new ApiResDto("비밀번호가 성공적으로 변경되었습니다." , 200));
    }
    @Secured({"ROLE_CUSTOMER", "ROLE_OWNER"})
    @PutMapping("/nickname")
    public ResponseEntity<ApiResDto> changeNickname(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody UserChangeNicknameReqDto request) {
        userService.changeNickname(userDetails, request);
        return ResponseEntity.ok(new ApiResDto("닉네임이 성공적으로 변경되었습니다.", 200));
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @GetMapping("/list")
    public ResponseEntity<PageResDto<PagedUserResponseDTO>> getUsers(
            @ModelAttribute UserSearchReqDto searchDto,
            @RequestParam(defaultValue = "10") int size,
            @PageableDefault(sort = {"createdAt", "modifiedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {

        return userService.getPagedUsers(searchDto, pageable, size);
    }
}
