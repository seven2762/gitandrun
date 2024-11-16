package com.sparta.gitandrun.user.dto.request;

import com.sparta.gitandrun.user.entity.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchReqDto {
    private String username;
    private String email;
    private String phone;
    private String nickName;
    private Role role;
}