package com.sparta.gitandrun.user.repository;

import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> searchUsers(String username, String email, String phone, String nickName, Role role, Pageable pageable);
}