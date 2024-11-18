package com.sparta.gitandrun.user.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    DUPLICATED_USER(400,"이미 가입된 회원 입니다."),
    DUPLICATED_USERNAME(400,"이미 사용준인 아이디 입니다."),
    DUPLICATED_NICKNAME(400,"이미 사용중인 닉네임 입니다."),
    DUPLICATED_EMAIL(400,"이미 사용중인 이메일 입니다."),
    USER_NOT_FOUND(400, "회원을 찾을 수 없습니다"),
    INVALID_PASSWORD(400,"잘못된 비밀번호 입니다." ),
    SAME_PASSWORD(400, "새 비밀번호가 현재 비밀번호와 동일합니다."),
    PASSWORD_MISMATCH(400, "새 비밀번호가 일치하지 않습니다."),
    SAME_NICKNAME(400,"새 닉네임이 현재 닉네임과 동일합니다"),
    DUPLICATE_NICKNAME(400, "이미 사용중인 닉네임 입니다.");
    private final int code;
    private final String message;


    ErrorCode(int num, String text) {
        code = num;
        message = text;
    }
}
