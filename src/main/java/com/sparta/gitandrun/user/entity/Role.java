package com.sparta.gitandrun.user.entity;


import lombok.Getter;
//enum은 @RequiredArgsConstructor 사용 불가
//@RequiredArgsConstructor는 객체 생성 시점에 동작
//enum은 클래스 로더에 의해 상수에서 이미 생성자가 사용되고 있음
@Getter
public enum Role {

    CUSTOMER("ROLE_CUSTOMER"),
    OWNER("ROLE_OWNER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}