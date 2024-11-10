package com.sparta.gitandrun.store.repository;

import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    List<Store> findByisDeletedFalse();
    Optional<Store> findById(UUID storeId); // Store의 ID로 조회하는 메서드
    List<Store> findByUser_UserId(UUID userId);  // userId로 Store 조회
}
