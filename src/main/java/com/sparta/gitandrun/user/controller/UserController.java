package com.sparta.gitandrun.user.controller;


import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.user.dto.request.LoginReqDto;
import com.sparta.gitandrun.user.dto.request.SignUpReqDTO;
import com.sparta.gitandrun.user.dto.response.LoginResDto;
import com.sparta.gitandrun.user.dto.response.SignUpResDTO;
import com.sparta.gitandrun.user.dto.response.UserApiResDto;
import com.sparta.gitandrun.user.dto.response.UserResDTO;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import com.sparta.gitandrun.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserApiResDto> signup(@RequestBody SignUpReqDTO signUpReqDTO) {

        SignUpResDTO signUpResDTO = userService.signUp(signUpReqDTO);

        return ResponseEntity.ok(new UserApiResDto("회원 가입이 완료되었습니다.", 200, signUpResDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserApiResDto> login(@RequestBody LoginReqDto loginReqDto) {
        LoginResDto loginResDto = userService.login(loginReqDto);
        return ResponseEntity.ok(new UserApiResDto("로그인이 완료되었습니다.", 200, loginResDto));
    }

    @GetMapping("/all")
    public ResponseEntity<UserApiResDto> getAllUsers() {
        List<UserResDTO> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(new UserApiResDto("회원 목록 조회 성공", 200, users));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResDto> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String password
    ) {
        User user = userDetails.user();
        userService.updatePassword(user, password);
        return ResponseEntity.ok(new ApiResDto("비밀번호가 성공적으로 변경되었습니다.", 200));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResDto> softDeleteUser(@RequestParam String phone) {
        userService.softDeleteUser(phone);
        return ResponseEntity.ok(new ApiResDto("회원 소프트 딜리트 완료", 200));
    }

}
