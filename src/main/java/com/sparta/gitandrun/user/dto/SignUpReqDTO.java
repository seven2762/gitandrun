package com.sparta.gitandrun.user.dto;

import com.sparta.gitandrun.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDTO {
    private Long userId;
    private String username;
    private String nickName;
    private String email;
    private String password;
    private Role role;
    private AddressDTO addressReq;
    private String phone;

}
