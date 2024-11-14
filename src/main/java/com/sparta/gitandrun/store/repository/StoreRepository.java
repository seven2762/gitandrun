package com.sparta.gitandrun.store.repository;

import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    List<Store> findByisDeletedFalse();
    Optional<Store> findById(UUID storeId); // Store의 ID로 조회하는 메서드
    List<Store> findByUser_UserId(Long userId);

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword% OR s.address.address LIKE %:keyword%")
    Page<Store> searchStores(@Param("keyword") String keyword, Pageable pageable);

    Page<Store> findByCategoryId(UUID categoryId, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword% OR s.address.address LIKE %:keyword%")
    Page<Store> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 지역 이름으로 가게 검색
    @Query("SELECT s FROM Store s WHERE s.region.regionName LIKE %:regionName%")
    List<Store> findByRegionName(@Param("regionName") String regionName);

    // 페이징 처리와 함께 지역 이름으로 가게 검색
    @Query("SELECT s FROM Store s WHERE s.region.regionName LIKE %:regionName%")
    Page<Store> findByRegionNameWithPagination(@Param("regionName") String regionName, Pageable pageable);
}
