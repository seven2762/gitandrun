package com.sparta.gitandrun.user.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    DUPLICATED_USER(400,"이미 가입된 회원 입니다."),
    USER_NOT_FOUND(400, "회원을 찾을 수 없습니다"),
    INVALID_PASSWORD(400,"잘못된 비밀번호 입니다." );

    private final int code;
    private final String message;


    ErrorCode(int num, String text) {
        code = num;
        message = text;
    }
}
