package com.sparta.gitandrun.user.service;

import com.sparta.gitandrun.user.dto.request.LoginReqDto;
import com.sparta.gitandrun.user.dto.request.SignUpReqDTO;
import com.sparta.gitandrun.user.dto.response.LoginResDto;
import com.sparta.gitandrun.user.dto.response.SignUpResDTO;
import com.sparta.gitandrun.user.dto.response.UserResDTO;
import com.sparta.gitandrun.user.entity.Address;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.exception.ErrorCode;
import com.sparta.gitandrun.user.exception.UserException;
import com.sparta.gitandrun.user.jwt.JwtUtil;
import com.sparta.gitandrun.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;  // 추가
    private final JwtUtil jwtUtil;

    public SignUpResDTO signUp(SignUpReqDTO signUpReqDTO) {

        Optional<User> isDuplicated = userRepository.findByPhone(signUpReqDTO.getPhone());
        if (isDuplicated.isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_USER);
        }
        User user = User.builder()
                .username(signUpReqDTO.getUsername())
                .nickName(signUpReqDTO.getNickName())
                .email(signUpReqDTO.getEmail())
                .password(passwordEncoder.encode(signUpReqDTO.getPassword()))
                .address(new Address(  // embedded
                        signUpReqDTO.getAddressReq().getAddress(),
                        signUpReqDTO.getAddressReq().getAddressDetail(),
                        signUpReqDTO.getAddressReq().getZipcode()
                ))
                .role(Role.CUSTOMER)
                .phone(signUpReqDTO.getPhone())

                .build();
        userRepository.save(user);



        return SignUpResDTO.from("good~!");
    }
    public LoginResDto login(LoginReqDto loginReqDTO) {
        User user = userRepository.findByEmail(loginReqDTO.getEmail())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(loginReqDTO.getPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }
        if (user.isDeleted()) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
        return LoginResDto.of(user, jwtUtil.createToken(user.getEmail(), user.getRole()));
    }


    public List<UserResDTO> getAllActiveUsers() {
        List<User> activeUsers = userRepository.findAllActiveUsers();
        return activeUsers.stream()
                .map(UserResDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        user.updatePassword(encodedPassword, String.valueOf(user.getUserId()));
    }

    @Transactional
    public void softDeleteUser(String phone) {
        User user = userRepository.findActiveUserByPhone(phone)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.softDelete();
    }
}


