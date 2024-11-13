package com.sparta.gitandrun.store.service;

import com.sparta.gitandrun.store.dto.FullStoreResponse;
import com.sparta.gitandrun.store.dto.LimitedStoreResponse;
import com.sparta.gitandrun.store.dto.StoreRequestDto;
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
        User user = validateUserWithRoles(userId, Role.ADMIN, Role.OWNER);

        Store newStore = new Store();
        populateStoreFields(newStore, storeRequestDto, user.getUserId().toString());

        return storeRepository.save(newStore);
    }

    // 전체 가게 조회
    public List<?> getAllStores(Long userId) {
        User user = getUser(userId);
        List<Store> allStores = user.getRole() == Role.ADMIN ?
                storeRepository.findAll() : storeRepository.findByisDeletedFalse();

        return allStores.stream()
                .map(store -> user.getRole() == Role.ADMIN ? new FullStoreResponse(store) : new LimitedStoreResponse(store))
                .collect(Collectors.toList());
    }

    // 가게 상세 정보 조회
    public ResponseEntity<?> getStoreDetails(UUID storeId, Long userId) {
        User user = getUser(userId);
        Store store = getStore(storeId);

        return user.getRole() == Role.ADMIN ?
                ResponseEntity.ok(new FullStoreResponse(store)) :
                ResponseEntity.ok(generateLimitedStoreDetails(store));
    }

    // 가게 수정
    @Transactional
    public void updateStore(UUID storeId, Long userId, StoreRequestDto updatedStore) {
        User user = validateUserWithRoles(userId, Role.ADMIN, Role.OWNER);
        Store existingStore = getStore(storeId);

        populateStoreFields(existingStore, updatedStore, user.getUserId().toString());

        storeRepository.save(existingStore);
    }

    // 가게 삭제 (soft delete 방식)
    @Transactional
    public boolean deleteStore(UUID storeId, Long userId) {
        User user = validateUserWithRoles(userId, Role.ADMIN, Role.OWNER);
        Store store = getStore(storeId);

        store.markAsDeleted(user.getUserId().toString());
        storeRepository.save(store);
        return true;
    }

    // 페이징 및 조건부 정렬 구현
    public Page<?> searchStores(UUID categoryId, String sortField, int page, int size, Long userId) {
        User user = getUser(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        Page<Store> stores = storeRepository.findByCategoryId(categoryId, pageable);

        return mapStoreResponse(stores, user.getRole());
    }

    // 키워드 검색 및 권한에 따른 응답 제어
    public Page<?> searchStoresByKeyword(String keyword, String sortField, int page, int size, Long userId) {
        User user = getUser(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        Page<Store> stores = storeRepository.searchByKeyword(keyword, pageable);

        return mapStoreResponse(stores, user.getRole());
    }

    // 유저 조회 메서드
    private User getUser(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 가게 조회 메서드
    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
    }

    private User validateUserWithRoles(Long userId, Role... roles) {
        User user = getUser(userId);
        for (Role role : roles) {
            if (user.getRole() == role) {
                return user;
            }
        }
        throw new IllegalArgumentException("요청 권한이 없습니다.");
    }

    private Map<String, Object> generateLimitedStoreDetails(Store store) {
        Map<String, Object> filteredStoreDetails = new LinkedHashMap<>();
        filteredStoreDetails.put("storeName", store.getStoreName());
        filteredStoreDetails.put("phone", store.getPhone());
        filteredStoreDetails.put("address", store.getAddress());
        filteredStoreDetails.put("addressDetail", store.getAddressDetail());
        filteredStoreDetails.put("zipCode", store.getZipCode());
        return filteredStoreDetails;
    }

    private Page<?> mapStoreResponse(Page<Store> stores, Role role) {
        return role == Role.ADMIN ?
                stores.map(FullStoreResponse::new) :
                stores.map(LimitedStoreResponse::new);
    }

    private void populateStoreFields(Store store, StoreRequestDto storeRequestDto, String userId) {
        if (storeRequestDto.getStoreName() != null) store.setStoreName(storeRequestDto.getStoreName());
        if (storeRequestDto.getPhone() != null) store.setPhone(storeRequestDto.getPhone());
        if (storeRequestDto.getCategory() != null) store.setCategory(storeRequestDto.getCategory());
        if (storeRequestDto.getAddress() != null) store.setAddress(storeRequestDto.getAddress());
        if (storeRequestDto.getAddressDetail() != null) store.setAddressDetail(storeRequestDto.getAddressDetail());
        if (storeRequestDto.getZipCode() != null) store.setZipCode(storeRequestDto.getZipCode());
        store.setUpdatedBy(userId);
        store.setUpdatedAt(LocalDateTime.now());
    }
}
