package com.sparta.gitandrun.user.entity;

import com.sparta.gitandrun.common.entity.BaseEntity;
import com.sparta.gitandrun.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

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
    private List<Store> stores;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;
    //Auditing 추후 구현 예정

    @PostPersist
    public void prePersistCreatedBy() {
        setCreatedBy(String.valueOf(this.userId));
    }


    public void updatePassword(String password, String updatedBy) {
        this.password = password;
        setUpdatedBy(updatedBy);
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}