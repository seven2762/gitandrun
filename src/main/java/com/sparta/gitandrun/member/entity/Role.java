package com.sparta.gitandrun.member.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    CUSTOMER("ROLE_CUSTOMER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN");

    private final String role;
}
