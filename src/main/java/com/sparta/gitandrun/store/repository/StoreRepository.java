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
    Optional<Store> findById(UUID storeId); // Store의 ID로 조회하는 메서드

    List<Store> findByUser_UserId(Long userId);

    // Soft-Delete된 가게만 조회
    List<Store> findByIsDeletedTrue();

    // Soft-Delete되지 않은 가게만 조회
    List<Store> findByIsDeletedFalse();

    @Query("SELECT s FROM Store s WHERE s.category.id = :categoryId")
    Page<Store> findByCategoryId(UUID categoryId, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.category.id = :categoryId AND s.isDeleted = false")
    Page<Store> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword% OR s.phone LIKE %:keyword%")
    Page<Store> searchStores(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.region.regionName LIKE %:regionName%")
    List<Store> findByRegionName(@Param("regionName") String regionName);

    @Query("SELECT s FROM Store s " +
            "WHERE (LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.address.address) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.address.addressDetail) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:isAdmin = true OR s.isDeleted = false)")
    Page<Store> searchStoresWithKeywordAndRole(@Param("keyword") String keyword,
                                               @Param("isAdmin") boolean isAdmin,
                                               Pageable pageable);

    // Soft-Delete된 가게 카테고리로 검색
    @Query("SELECT s FROM Store s WHERE s.category.id = :categoryId AND s.isDeleted = true")
    Page<Store> findByCategoryIdAndIsDeletedTrue(@Param("categoryId") UUID categoryId, Pageable pageable);

    // Soft-Delete된 가게 키워드로 검색
    @Query("SELECT s FROM Store s WHERE " +
            "(LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.address.address) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.address.addressDetail) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND s.isDeleted = true")
    Page<Store> searchStoresAndIsDeletedTrue(@Param("keyword") String keyword, Pageable pageable);

    // Soft-Delete되지 않은 가게 키워드로 검색
    @Query("SELECT s FROM Store s WHERE " +
            "(LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.address.address) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.address.addressDetail) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND s.isDeleted = false")
    Page<Store> searchStoresAndIsDeletedFalse(@Param("keyword") String keyword, Pageable pageable);
}
