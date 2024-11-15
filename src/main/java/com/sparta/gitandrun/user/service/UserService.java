package com.sparta.gitandrun.user.service;

import com.sparta.gitandrun.user.dto.request.SignUpReqDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
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
        Role role = validateAndGetRole(signUpReqDTO.getRole());
        User user = User.createUser(signUpReqDTO, role, passwordEncoder);
        userRepository.save(user);
        return SignUpResDTO.from("good~!");
    }

    public Role validateAndGetRole(Role requestRole) {
        if (requestRole == Role.ADMIN) {
            return Role.ADMIN;
        } else if (requestRole == Role.OWNER) {
            return Role.OWNER;
        } else {
            return Role.CUSTOMER;
        }
    }
    public List<UserResDTO> getAllActiveUsers() {
        List<User> activeUsers = userRepository.findAllActiveUsers();
        return activeUsers.stream()
                .map(UserResDTO::from)
                .collect(Collectors.toList());
    }
    @Transactional
    public void updatePassword(User user, Map<String, String> request) {
        String encodedPassword = passwordEncoder.encode(request.get("password"));
        user.updatePassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void softDeleteUser(String phone) {
        User user = userRepository.findActiveUserByPhone(phone)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.softDelete();
    }
}


