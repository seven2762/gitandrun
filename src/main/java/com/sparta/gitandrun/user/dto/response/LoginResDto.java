package com.sparta.gitandrun.user.dto.response;

import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResDto {

    private String email;
    private String password;
    private String token;
    private Role role;

    public static LoginResDto of(User user, String token) {
        return LoginResDto.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
