package com.sparta.gitandrun.user.repository;

import com.sparta.gitandrun.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , UserRepositoryCustom {

    Optional<User> findByPhone(String phone);


    @Query("select u from User u where u.isDeleted = false and u.phone = :phone")
    Optional<User> findActiveUserByPhone(@Param("phone") String phone);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByNickName(String nickName);
}
