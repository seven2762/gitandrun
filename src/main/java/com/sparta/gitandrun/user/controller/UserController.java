package com.sparta.gitandrun.user.controller;


import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.user.dto.SignUpReqDTO;
import com.sparta.gitandrun.user.dto.SignUpResDTO;
import com.sparta.gitandrun.user.dto.UserResDTO;
import com.sparta.gitandrun.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResDto> signup(@RequestBody SignUpReqDTO signUpReqDTO) {

        SignUpResDTO signUpResDTO = userService.signUp(signUpReqDTO);

        return ResponseEntity.ok(new ApiResDto("회원 가입이 완료되었습니다.", 200));
    }

    @GetMapping
    public ResponseEntity<ApiResDto> getAllUsers() {
        List<UserResDTO> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(new ApiResDto("회원 목록 조회 성공", 200));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResDto> updateUser(@RequestParam String phone, @RequestParam String password) {
        userService.updatePassword(phone, password);
        return ResponseEntity.ok(new ApiResDto("비밀번호가 성공적으로 변경되었습니다.", 200));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResDto> softDeleteUser(@RequestParam String phone) {
        userService.softDeleteUser(phone);
        return ResponseEntity.ok(new ApiResDto("회원 소프트 딜리트 완료", 200));
    }

}
