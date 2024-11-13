package com.sparta.gitandrun.user.dto.request;

import com.sparta.gitandrun.user.dto.AddressDTO;
import com.sparta.gitandrun.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
