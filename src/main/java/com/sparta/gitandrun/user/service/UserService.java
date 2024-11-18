package com.sparta.gitandrun.user.service;

import com.sparta.gitandrun.user.dto.request.SignUpReqDTO;
import com.sparta.gitandrun.user.dto.request.UserChangeNicknameReqDto;
import com.sparta.gitandrun.user.dto.request.UserChangePasswordReqDto;
import com.sparta.gitandrun.user.dto.request.UserSearchReqDto;
import com.sparta.gitandrun.user.dto.response.*;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.exception.ErrorCode;
import com.sparta.gitandrun.user.exception.UserException;
import com.sparta.gitandrun.user.jwt.JwtUtil;
import com.sparta.gitandrun.user.repository.UserRepository;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Builder
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;  // 추가
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResDTO signUp(SignUpReqDTO signUpReqDTO) {

        validateDuplicateUser(signUpReqDTO);
        validateDuplicateNickname(signUpReqDTO.getNickName()); // 회원가입시에는 현재 닉네임이 없으므로 null

        Role role = validateAndGetRole(signUpReqDTO.getRole());
        User user = User.createUser(signUpReqDTO, role, passwordEncoder);
        userRepository.save(user);
        return SignUpResDTO.from("good~!");
    }
    //닉네임 중복확인
    private void validateDuplicateNickname(String nickname) {
        if (userRepository.findByNickName(nickname).isPresent()) {
            throw new UserException(ErrorCode.DUPLICATE_NICKNAME);
        }
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
    //회원가입 검증 메서드
    private void validateDuplicateUser(SignUpReqDTO signUpReqDTO) {
        if (userRepository.findByPhone(signUpReqDTO.getPhone()).isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_USER);
        }

        if (userRepository.findByUsername(signUpReqDTO.getUsername()).isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_USERNAME);
        }

        if (userRepository.findByNickName(signUpReqDTO.getNickName()).isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_NICKNAME);
        }

        if (userRepository.findByEmail(signUpReqDTO.getEmail()).isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_EMAIL);
        }
    }


    @Transactional
    public void softDeleteUser(String phone) {
        User user = userRepository.findActiveUserByPhone(phone)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.softDelete();
    }
    @Transactional
    public void changePassword(UserDetailsImpl userDetails, UserChangePasswordReqDto request) {

        User user = userRepository.findById(userDetails.getUser().getUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        request.validate(userDetails.getUser(), passwordEncoder);
        // 영속 상태의 엔티티 수정 -> 더티체킹 동작
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void changeNickname(UserDetailsImpl userDetails, UserChangeNicknameReqDto request) {
        User user = userRepository.findById(userDetails.getUser().getUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if (request.getNewNickname().equals(user.getNickName())) {
            throw new UserException(ErrorCode.SAME_NICKNAME);
        }
        validateDuplicateNickname(request.getNewNickname());
        user.changeNickname(request.getNewNickname());
    }


    @Transactional(readOnly = true)
    public ResponseEntity<PageResDto<PagedUserResponseDTO>> getPagedUsers(UserSearchReqDto searchDto, Pageable pageable, int size) {
        int validatedSize = validatePageSize(size);
        pageable = PageRequest.of(pageable.getPageNumber(), validatedSize, pageable.getSort());

        Page<User> userPage = userRepository.searchUsers(
                searchDto.getUsername(),
                searchDto.getEmail(),
                searchDto.getPhone(),
                searchDto.getNickName(),
                searchDto.getRole(),
                pageable
        );

        return new ResponseEntity<>(
                PageResDto.<PagedUserResponseDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("사용자 목록 조회 성공")
                        .data(PagedUserResponseDTO.of(userPage))
                        .build(),
                HttpStatus.OK
        );
    }


    public int validatePageSize(int size) {
        return (size == 10 || size == 30 || size == 50) ? size : 10;
    }
}


