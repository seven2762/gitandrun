package com.sparta.gitandrun.store.service;

import com.sparta.gitandrun.store.dto.FullStoreResponse;
import com.sparta.gitandrun.store.dto.LimitedStoreResponse;
import com.sparta.gitandrun.store.dto.StoreResponseDto;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.store.repository.StoreRepository;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    // 가게 생성
    @Transactional
    public Store createStore(UUID userId, Store newStore) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 권한 확인
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.OWNER && user.getRole() != User.Role.MASTER) {
            throw new IllegalArgumentException("생성 권한이 없습니다.");
        }

        // 자동으로 createdBy와 updatedBy 설정
        newStore.setCreatedBy(user.getUserId().toString());
        newStore.setUpdatedBy(user.getUserId().toString());
        newStore.setCreatedAt(LocalDateTime.now());
        newStore.setUpdatedAt(LocalDateTime.now());

        // 가게 저장
        return storeRepository.save(newStore);
    }


    // 전체 가게 조회
    public List<?> getAllStores(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        User.Role userRole = user.getRole();
        List<Store> allStores;

        // ADMIN 역할일 경우 삭제된 가게 포함 모든 가게를 조회
        if (userRole == User.Role.ADMIN) {
            allStores = storeRepository.findAll();
            return allStores.stream()
                    .map(store -> new FullStoreResponse(store))
                    .collect(Collectors.toList());
        }

        // ADMIN이 아닌 경우 삭제되지 않은 가게만 조회
        allStores = storeRepository.findByisDeletedFalse();
        return allStores.stream()
                .map(store -> new LimitedStoreResponse(store))
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> getStoreDetails(UUID storeId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (user.getRole() == User.Role.ADMIN) {
            return ResponseEntity.ok(store);
        } else {
            // LinkedHashMap을 사용하여 필드 순서를 지정
            Map<String, Object> filteredStoreDetails = new LinkedHashMap<>();
            filteredStoreDetails.put("storeName", store.getStoreName());
            filteredStoreDetails.put("phone", store.getPhone());
            filteredStoreDetails.put("address", store.getAddress());
            filteredStoreDetails.put("addressDetail", store.getAddressDetail());
            filteredStoreDetails.put("zipCode", store.getZipCode());

            return ResponseEntity.ok(filteredStoreDetails);
        }
    }



    // 가게 수정
    @Transactional
    public void updateStore(UUID storeId, UUID userId, Store updatedStore) {
        Store existingStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

        // 권한 체크
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.OWNER && user.getRole() != User.Role.MASTER) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // 수정할 필드만 업데이트
        if (updatedStore.getStoreName() != null) {
            existingStore.setStoreName(updatedStore.getStoreName());
        }
        if (updatedStore.getPhone() != null) {
            existingStore.setPhone(updatedStore.getPhone());
        }
        if (updatedStore.getCategory() != null) {
            existingStore.setCategory(updatedStore.getCategory());
        }
        if (updatedStore.getAddress() != null) {
            existingStore.setAddress(updatedStore.getAddress());
        }
        if (updatedStore.getAddressDetail() != null) {
            existingStore.setAddressDetail(updatedStore.getAddressDetail());
        }
        if (updatedStore.getZipCode() != null) {
            existingStore.setZipCode(updatedStore.getZipCode());
        }
        existingStore.setUpdatedBy(userId.toString());
        existingStore.setUpdatedAt(LocalDateTime.now());

        storeRepository.save(existingStore);
    }

    // 가게 삭제 (soft delete 방식)
    @Transactional
    public boolean deleteStore(UUID storeId, UUID userId) {
        // userId로 User 조회
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        User user = userOptional.get();

        // Role 확인
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.OWNER && user.getRole() != User.Role.MASTER) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            store.markAsDeleted(user.getUserId().toString());  // userId로 삭제자 설정
            storeRepository.save(store);
            return true;
        }
        return false;
    }
}
