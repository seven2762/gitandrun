package com.sparta.gitandrun.store.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.store.dto.FullStoreResponse;
import com.sparta.gitandrun.store.dto.LimitedStoreResponse;
import com.sparta.gitandrun.store.dto.StoreRequestDto;
import com.sparta.gitandrun.store.dto.StoreSearchRequestDto;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.store.repository.StoreRepository;
import com.sparta.gitandrun.store.service.StoreService;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.repository.UserRepository;
import com.sparta.gitandrun.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public StoreController(StoreService storeService, UserRepository userRepository, StoreRepository storeRepository) {
        this.storeService = storeService;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    // 가게 등록
    @PostMapping
    public Store createStore(Long userId, @RequestBody StoreRequestDto storeRequestDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("생성 권한이 없습니다.");
        }

        Store newStore = new Store();
        newStore.setStoreName(storeRequestDto.getStoreName());
        newStore.setPhone(storeRequestDto.getPhone());
        newStore.setCategory(storeRequestDto.getCategory());
        newStore.setAddress(storeRequestDto.getAddress());
        newStore.setAddressDetail(storeRequestDto.getAddressDetail());
        newStore.setZipCode(storeRequestDto.getZipCode());
        newStore.setUser(user);  // user 설정하여 user_id 필드가 null이 되지 않도록 설정

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


    // ID로 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreDetails(
            @PathVariable UUID storeId,
            @RequestParam Long userId
    ) {
        return storeService.getStoreDetails(storeId, userId);
    }

    // 전체 가게 조회
    @GetMapping
    public ResponseEntity<?> getAllStores(@RequestParam Long userId) {
        List<?> stores = storeService.getAllStores(userId);
        return ResponseEntity.ok(stores);
    }

    // 가게 수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable UUID storeId,
                                         @RequestParam Long userId,
                                         @RequestBody StoreRequestDto updatedStoreDto) {
        try {
            storeService.updateStore(storeId, userId, updatedStoreDto);
            return ResponseEntity.ok(new ApiResDto("가게 정보가 성공적으로 수정되었습니다.", 200));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResDto("수정 권한이 없습니다.", 403));
        }
    }

    // 삭제 기능
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResDto> deleteStore(
            @PathVariable UUID storeId,
            @RequestParam Long userId
    ) {
        try {
            storeService.deleteStore(storeId, userId);
            return ResponseEntity.ok(new ApiResDto("가게 삭제가 완료되었습니다.", 200));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResDto("삭제 권한이 없습니다.", 403));
        }
    }

    // 카테고리로 검색
    @GetMapping("/search")
    public ResponseEntity<Page<?>> searchStores(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String role) {

        Page<?> stores = storeService.searchStores(categoryId, sortField, page, size, role);
        return ResponseEntity.ok(stores);
    }

    // 키워드로 검색
    @GetMapping("/search/keyword")
    public ResponseEntity<Page<?>> searchStoresByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam String role) {

        Page<?> stores = storeService.searchStoresByKeyword(keyword, sortField, page, size, role); // Page<Store>로 리턴
        return ResponseEntity.ok(stores);
    }
}
