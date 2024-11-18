package com.sparta.gitandrun.user.dto.request;


import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserChangeNicknameReqDto {

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "한글과 영문 대소문자 숫자로 2-10자리만 가능합니다")

    private String newNickname;

}
