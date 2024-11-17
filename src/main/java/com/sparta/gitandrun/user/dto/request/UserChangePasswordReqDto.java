package com.sparta.gitandrun.user.dto.request;

import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.exception.ErrorCode;
import com.sparta.gitandrun.user.exception.UserException;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class UserChangePasswordReqDto {


    @NotBlank(message = "새 비밀번호는 필수입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8~15자의 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String confirmPassword;

    public void validate(User user, PasswordEncoder passwordEncoder) {
        validateSamePassword(user, passwordEncoder);
        validatePasswordMatch();
    }

    private void validateSamePassword(User user, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserException(ErrorCode.SAME_PASSWORD);
        }
    }
    private void validatePasswordMatch() {
        if (!newPassword.equals(confirmPassword)) {
            throw new UserException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
