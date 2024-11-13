package com.sparta.gitandrun.user.dto.response;

import lombok.Getter;

@Getter
public class SignUpResDTO {

    private final String message;

    //private로 외부에서 객체 생성 막음 (캡슐화)
    private SignUpResDTO(String message) {
        this.message = message;
    }


    // 정적 팩토리 메서드
    // 도메인에서 “객체 생성”의 역할 자체가 중요한 경우라면
    // 정적 팩토리 클래스를 따로 분리하는 것도 좋은 방법이 될 것
    public static SignUpResDTO from(String message) {
        return new SignUpResDTO(message);
    }
}
