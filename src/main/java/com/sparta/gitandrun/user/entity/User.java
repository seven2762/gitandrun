package com.sparta.gitandrun.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;

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

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;
    //Auditing 추후 구현 예정


    public void updatePassword(String password) {
        this.password = password;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}