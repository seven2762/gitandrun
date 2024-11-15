package com.sparta.gitandrun.store.service;

import com.sparta.gitandrun.category.entity.Category;
import com.sparta.gitandrun.category.repository.CategoryRepository;
import com.sparta.gitandrun.region.entity.Region;
import com.sparta.gitandrun.region.repository.RegionRepository;
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

import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@Slf4j
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;

    public StoreService(StoreRepository storeRepository, UserRepository userRepository, RegionRepository regionRepository, CategoryRepository categoryRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Store createStore(Long userId, StoreRequestDto storeRequestDto) {
        // 사용자 정보 조회
        User user = getUser(userId);

        // 카테고리 이름으로 카테고리 조회
        Category category = categoryRepository.findByName(storeRequestDto.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + storeRequestDto.getCategoryName()));

        // regionId로 Region 조회
        Region region = regionRepository.findById(storeRequestDto.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다."));

        // 새 가게 생성
        Store newStore = new Store();

        // storeId를 UUID로 자동 생성
        newStore.setStoreId(UUID.randomUUID());

        // 필드 채우기
        populateStoreFields(newStore, storeRequestDto, user.getUserId().toString());

        // 카테고리와 지역 설정
        newStore.setCategory(category);
        newStore.setRegion(region);

        // created_by와 updated_by에 userId 설정
        newStore.setCreatedBy(user.getUserId().toString());
        newStore.setUpdatedBy(user.getUserId().toString());

        // user 설정
        newStore.setUser(user);

        // 새 가게 저장
        return storeRepository.save(newStore);
    }

    private void populateStoreFields(Store store, StoreRequestDto storeRequestDto, String userId) {
        store.setStoreName(storeRequestDto.getStoreName());
        store.setPhone(storeRequestDto.getPhone());
        store.setAddress(storeRequestDto.getAddress());
        store.setCreatedBy(userId);
        store.setUpdatedBy(userId);
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedAt(LocalDateTime.now());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // ADMIN 전체 가게 조회
    @Transactional(readOnly = true)
    public List<FullStoreResponse> getAllStoresForAdmin() {
        // 모든 가게 정보 조회
        List<Store> stores = storeRepository.findAll();
        // FullStoreResponse DTO로 변환하여 반환
        return stores.stream()
                .map(FullStoreResponse::new)
                .collect(Collectors.toList());
    }

    // ~ADMIN 전체 가게 조회
    @Transactional(readOnly = true)
    public List<LimitedStoreResponse> getAllStoresForUser() {
        // Soft-delete되지 않은 가게만 조회
        List<Store> stores = storeRepository.findByIsDeletedFalse();
        // LimitedStoreResponse DTO로 변환하여 반환
        return stores.stream()
                .map(LimitedStoreResponse::new)
                .collect(Collectors.toList());
    }

    // 가게 상세 정보 조회 (로그인한 유저의 권한에 따라 다르게 응답)
    @Transactional(readOnly = true)
    public ResponseEntity<?> getStoreDetails(UUID storeId, Long userId) {
        User user = getUser(userId); // 로그인한 유저 조회
        Store store = getStore(storeId); // 가게 정보 조회

        // 유저의 역할에 따라 FullStoreResponse 또는 LimitedStoreResponse 반환
        if (user.getRole() == Role.ADMIN) {
            return ResponseEntity.ok(new FullStoreResponse(store)); // 관리자일 경우 모든 정보 제공
        } else {
            return ResponseEntity.ok(generateLimitedStoreDetails(store)); // 그 외 유저는 제한된 정보만 제공
        }
    }

    // 관리자 가게 수정
    @Transactional
    public void updateStoreByAdmin(UUID storeId, StoreRequestDto storeRequestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 모든 필드 수정 가능
        store.setStoreName(storeRequestDto.getStoreName());
        store.setPhone(storeRequestDto.getPhone());
        store.setAddress(storeRequestDto.getAddress());

        if (storeRequestDto.getCategoryName() != null) {
            Category category = categoryRepository.findByName(storeRequestDto.getCategoryName())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
            store.setCategory(category);
        }

        if (storeRequestDto.getRegionId() != null) {
            Region region = regionRepository.findById(storeRequestDto.getRegionId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다."));
            store.setRegion(region);
        }
    }

    // 가게 수정
    @Transactional
    public void updateStoreByUser(UUID storeId, StoreRequestDto storeRequestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 제한된 필드만 수정 가능
        if (storeRequestDto.getStoreName() != null) {
            store.setStoreName(storeRequestDto.getStoreName());
        }
        if (storeRequestDto.getPhone() != null) {
            store.setPhone(storeRequestDto.getPhone());
        }
        if (storeRequestDto.getAddress() != null) {
            store.setAddress(storeRequestDto.getAddress());
        }
    }

    // ADMIN 권한으로 삭제
    @Transactional
    public void softDeleteStoreByAdmin(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        markAsDeleted(store, "ADMIN");
    }

    // Owner 권한으로 삭제
    @Transactional
    public void softDeleteStoreByOwner(UUID storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 소유한 가게만 삭제할 수 있습니다.");
        }

        markAsDeleted(store, userId.toString());
    }

    // Delete
    private void markAsDeleted(Store store, String deletedBy) {
        store.setDeletedBy(deletedBy);
        store.setDeletedAt(LocalDateTime.now());
        store.setDeleted(true); // Soft delete 플래그 설정
        storeRepository.save(store); // 변경된 상태 저장
    }

    // 카테고리로 검색
    public Page<?> searchStoresByCategory(UUID categoryId, String sortField, int page, int size, boolean isAdmin) {
        validatePageSize(size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortField));

        Page<Store> stores;
        if (isAdmin) {
            stores = storeRepository.findByCategoryId(categoryId, pageable);
        } else {
            stores = storeRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
        }

        return isAdmin ? stores.map(FullStoreResponse::new) : stores.map(LimitedStoreResponse::new);
    }


    private void validatePageSize(int size) {
        if (size != 10 && size != 30 && size != 50) {
            throw new IllegalArgumentException("허용되지 않는 페이지 크기입니다. (10, 30, 50 중 하나만 가능합니다)");
        }
    }

    public Page<?> searchStoresByKeyword(String keyword, String sortField, int page, int size, boolean isAdmin) {
        validatePageSize(size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortField));

        Page<Store> stores = storeRepository.searchStoresWithKeywordAndRole(keyword, isAdmin, pageable);

        // 관리자와 사용자에 따른 결과 매핑
        return isAdmin ? stores.map(FullStoreResponse::new) : stores.map(LimitedStoreResponse::new);
    }



    // 유저 조회 메서드
//    private User getUser(Long userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//    }

    // Store 조회 메서드 (storeId로 조회)
    private Store getStore(UUID storeId) {
        if (storeId == null) {
            throw new IllegalArgumentException("storeId는 null일 수 없습니다.");
        }
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
    }

    private User validateUserWithRoles(Long userId, Role... roles) {
        User user = getUser(userId); // 유저 정보 조회
        System.out.println("User Role: " + user.getRole());  // role 값 확인

        // ADMIN 권한만 체크
        if (user.getRole() == Role.ADMIN) {
            System.out.println("ADMIN 권한 허용.");
            return user;  // ADMIN 권한을 가진 사용자는 정상 처리
        }

        // ADMIN이 아니면 권한 없음
        System.out.println("권한이 없는 유저입니다.");
        throw new IllegalArgumentException("요청 권한이 없습니다.");
    }


    private Map<String, Object> generateLimitedStoreDetails(Store store) {
        Map<String, Object> filteredStoreDetails = new LinkedHashMap<>();
        filteredStoreDetails.put("storeName", store.getStoreName());
        filteredStoreDetails.put("phone", store.getPhone());
        filteredStoreDetails.put("address", store.getAddress().getAddress());
        filteredStoreDetails.put("addressDetail", store.getAddress().getAddressDetail());
        filteredStoreDetails.put("zipCode", store.getAddress().getZipCode());
        return filteredStoreDetails;
    }

    private Page<?> mapStoreResponse(Page<Store> stores, Role role) {
        return role == Role.ADMIN ?
                stores.map(FullStoreResponse::new) :
                stores.map(LimitedStoreResponse::new);
    }

//    private void populateStoreFields(Store store, StoreRequestDto storeRequestDto, String userId) {
//        if (storeRequestDto.getStoreName() != null) store.setStoreName(storeRequestDto.getStoreName());
//        if (storeRequestDto.getPhone() != null) store.setPhone(storeRequestDto.getPhone());
//
//        // 카테고리 이름이 비어있지 않으면 카테고리 조회 후 설정
//        if (storeRequestDto.getCategoryName() != null && !storeRequestDto.getCategoryName().isEmpty()) {
//            Category category = categoryRepository.findByName(storeRequestDto.getCategoryName())
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + storeRequestDto.getCategoryName()));
//            store.setCategory(category);
//        } else {
//            throw new IllegalArgumentException("카테고리 이름은 필수입니다.");
//        }
//
//        // 주소 처리
//        if (storeRequestDto.getAddress() != null) store.setAddress(storeRequestDto.getAddress());
//
//        store.setUpdatedBy(userId);
//        store.setUpdatedAt(LocalDateTime.now());
//    }

    // 지역 이름으로 가게 조회
    public List<?> getStoresByRegionName(Long userId, String regionName) {
        User user = getUser(userId);

        // 지역 이름으로 가게 검색
        List<Store> stores = storeRepository.findByRegionName(regionName);

        return stores.stream()
                .map(store -> user.getRole() == Role.ADMIN ? new FullStoreResponse(store) : new LimitedStoreResponse(store))
                .collect(Collectors.toList());
    }

}