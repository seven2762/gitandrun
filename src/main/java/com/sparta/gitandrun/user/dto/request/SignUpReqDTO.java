package com.sparta.gitandrun.user.dto.request;

import com.sparta.gitandrun.user.dto.AddressDTO;
import com.sparta.gitandrun.user.entity.Role;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDTO {
    @Pattern(
            regexp = "^[a-z0-9]{4,10}$",
            message = "아이디는 4~10자의 알파벳 소문자와 숫자로만 구성되어야 합니다."
    )
    private String username;


    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "한글과 영문 대소문자 숫자로 2-10자리만 가능합니다")
    private String nickName;
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 8~15자의 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;
    private Role role;
    private AddressDTO addressReq;
    private String phone;

}
