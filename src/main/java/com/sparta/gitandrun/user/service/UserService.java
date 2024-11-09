package com.sparta.gitandrun.user.service;

import com.sparta.gitandrun.user.dto.SignUpReqDTO;
import com.sparta.gitandrun.user.dto.SignUpResDTO;
import com.sparta.gitandrun.user.dto.UserResDTO;
import com.sparta.gitandrun.user.entity.Address;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.exception.ErrorCode;
import com.sparta.gitandrun.user.exception.UserException;
import com.sparta.gitandrun.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class UserService {

    private final UserRepository userRepository;

    public SignUpResDTO signUp(SignUpReqDTO SignUpReqDTO) {

        Optional<User> isDuplicated = userRepository.findByPhone(SignUpReqDTO.getPhone());
        if (isDuplicated.isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_USER);
        }
        User user = User.builder()
                .username(SignUpReqDTO.getUsername())
                .nickName(SignUpReqDTO.getNickName())
                .email(SignUpReqDTO.getEmail())
                .password(SignUpReqDTO.getPassword())
                .address(new Address(  // embedded
                        SignUpReqDTO.getAddressReq().getAddress(),
                        SignUpReqDTO.getAddressReq().getAddressDetail(),
                        SignUpReqDTO.getAddressReq().getZipcode()
                ))
                .role(Role.CUSTOMER) // 테스트 위해 기본값 고객으로 설정, 추후 수정
                .phone(SignUpReqDTO.getPhone())
                .build();
        userRepository.save(user);

        return SignUpResDTO.from("추후에 토큰 발급 가능하게 만들예정.");
    }

    //전체 회원 목록 조회
    public List<UserResDTO> getAllActiveUsers() {
        List<User> activeUsers = userRepository.findAllActiveUsers();
        return activeUsers.stream()
                .map(UserResDTO::from)
                .collect(Collectors.toList());
    }

    //비밀번호 변경
    @Transactional
    public void updatePassword(String phone, String password) {
        User user = userRepository.findActiveUserByPhone(phone)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.updatePassword(password);
    }
    @Transactional
    public void softDeleteUser(String phone) {
        User user = userRepository.findActiveUserByPhone(phone)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.softDelete();
    }
}


