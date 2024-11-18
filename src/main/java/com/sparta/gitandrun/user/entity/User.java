package com.sparta.gitandrun.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.gitandrun.common.entity.BaseEntity;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.user.dto.request.SignUpReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id" )
    private Long userId;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(name = "nick_name", nullable = false, length = 20, unique = true)
    private String nickName;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    // User가 Store 정보를 가져올 수 있게 추가
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // Store와의 관계 설정
    @JsonManagedReference // 순환 참조 방지
    private List<Store> stores;

    @Column(name = "is_deleted")
    private boolean isDeleted ;


    public static User createUser(SignUpReqDTO signUpReqDTO, Role role, BCryptPasswordEncoder passwordEncoder) {
        User user =  User.builder()
                .username(signUpReqDTO.getUsername())
                .nickName(signUpReqDTO.getNickName())
                .email(signUpReqDTO.getEmail())
                .password(passwordEncoder.encode(signUpReqDTO.getPassword()))
                .address(new Address(
                        signUpReqDTO.getAddressReq().getAddress(),
                        signUpReqDTO.getAddressReq().getAddressDetail(),
                        signUpReqDTO.getAddressReq().getZipcode()
                ))
                .role(role)
                .phone(signUpReqDTO.getPhone())
                .build();

        user.initAuditInfo(user);
        return user;
    }

    public void changePassword(String password) {
        this.password = password;
        this.initAuditInfo(this);
    }
    public void changeNickname(String nickname) {
        this.nickName = nickname;
        this.initAuditInfo(this);
    }
    public void softDelete() {
        this.isDeleted = true;
    }
}