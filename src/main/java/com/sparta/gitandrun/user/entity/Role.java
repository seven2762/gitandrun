package com.sparta.gitandrun.user.entity;


import lombok.Getter;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

//enum은 @RequiredArgsConstructor 사용 불가
//@RequiredArgsConstructor는 객체 생성 시점에 동작
//enum은 클래스 로더에 의해 상수에서 이미 생성자가 사용되고 있음

@Getter
public enum Role {
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN"),
    OWNER("ROLE_OWNER"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String role;
    private static final Map<Role, List<Role>> ROLE_LEVELS = new EnumMap<>(Role.class);

    static {
        ROLE_LEVELS.put(MANAGER, List.of(ADMIN, OWNER, CUSTOMER));
        ROLE_LEVELS.put(ADMIN, List.of(OWNER, CUSTOMER));
        ROLE_LEVELS.put(OWNER, Collections.emptyList());
        ROLE_LEVELS.put(CUSTOMER, Collections.emptyList());
    }

    Role(String role) {
        this.role = role;
    }

    public List<Role> getIncludeRoles() {
        return ROLE_LEVELS.getOrDefault(this, Collections.emptyList());
    }
}

