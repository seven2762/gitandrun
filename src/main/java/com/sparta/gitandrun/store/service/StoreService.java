package com.sparta.gitandrun.store.service;

import com.sparta.gitandrun.store.dto.FullStoreResponse;
import com.sparta.gitandrun.store.dto.LimitedStoreResponse;
import com.sparta.gitandrun.store.dto.StoreRequestDto;
import com.sparta.gitandrun.store.dto.StoreSearchRequestDto;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.store.repository.StoreRepository;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

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
    public Store createStore(Long userId, StoreRequestDto storeRequestDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("생성 권한이 없습니다.");
        }

        // Store 객체 생성 및 필드 설정
        Store newStore = new Store();
        newStore.setStoreName(storeRequestDto.getStoreName());
        newStore.setPhone(storeRequestDto.getPhone());
        newStore.setCategory(storeRequestDto.getCategory());
        newStore.setAddress(storeRequestDto.getAddress());
        newStore.setAddressDetail(storeRequestDto.getAddressDetail());
        newStore.setZipCode(storeRequestDto.getZipCode());

        // 카테고리가 설정되었을 경우 category_id 자동 할당
        if (newStore.getCategory() != null) {
            newStore.setCategoryId(newStore.getCategory().getUuid());
        }

        newStore.setCreatedBy(user.getUserId().toString());
        newStore.setUpdatedBy(user.getUserId().toString());
        newStore.setCreatedAt(LocalDateTime.now());
        newStore.setUpdatedAt(LocalDateTime.now());

        return storeRepository.save(newStore);
    }


    // 전체 가게 조회
    public List<?> getAllStores(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Store> allStores;

        if (user.getRole() == Role.ADMIN) {
            allStores = storeRepository.findAll();
            return allStores.stream()
                    .map(FullStoreResponse::new)
                    .collect(Collectors.toList());
        }

        allStores = storeRepository.findByisDeletedFalse();
        return allStores.stream()
                .map(LimitedStoreResponse::new)
                .collect(Collectors.toList());
    }

    // 가게 상세 정보 조회
    public ResponseEntity<?> getStoreDetails(UUID storeId, Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (user.getRole() == Role.ADMIN) {
            return ResponseEntity.ok(new FullStoreResponse(store));
        } else {
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
    public void updateStore(UUID storeId, Long userId, StoreRequestDto updatedStore) {
        Store existingStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

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
    public boolean deleteStore(UUID storeId, Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
        store.markAsDeleted(user.getUserId().toString());
        storeRepository.save(store);
        return true;
    }

    // 페이징 및 조건부 정렬 구현
    public Page<?> searchStores(UUID categoryId, String sortField, int page, int size, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        Page<Store> stores = storeRepository.findByCategoryId(categoryId, pageable);

        if (user.getRole() == Role.ADMIN) {
            return stores.map(FullStoreResponse::new); // 관리자: 모든 정보 표시
        } else {
            return stores.map(LimitedStoreResponse::new); // 소유자 및 고객: 제한된 정보만 표시
        }
    }

    // 키워드 검색 및 권한에 따른 응답 제어
    public Page<?> searchStoresByKeyword(String keyword, String sortField, int page, int size, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        Page<Store> stores = storeRepository.searchByKeyword(keyword, pageable);

        if (user.getRole() == Role.ADMIN) {
            return stores.map(FullStoreResponse::new); // 관리자: 모든 정보 표시
        } else {
            return stores.map(LimitedStoreResponse::new); // 소유자 및 고객: 제한된 정보만 표시
        }
    }
}