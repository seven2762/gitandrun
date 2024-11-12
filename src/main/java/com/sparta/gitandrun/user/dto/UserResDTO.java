package com.sparta.gitandrun.user.dto;

import com.sparta.gitandrun.user.entity.Address;
import com.sparta.gitandrun.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResDTO {

    private Long userId;
    private String username;
    private String nickName;
    private String email;
    private String phone;
    private Address address;

    public static UserResDTO from(User user) {
        return UserResDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }
}
