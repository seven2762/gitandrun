package com.sparta.gitandrun.store.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.store.dto.FullStoreResponse;
import com.sparta.gitandrun.store.dto.LimitedStoreResponse;
import com.sparta.gitandrun.store.dto.StoreRequestDto;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.store.repository.StoreRepository;
import com.sparta.gitandrun.store.service.StoreService;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import com.sparta.gitandrun.user.repository.UserRepository;
import com.sparta.gitandrun.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    @Transactional
    public Store createStore(UUID userId, @RequestBody StoreRequestDto storeRequestDto) {
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



    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreDetails(
            @PathVariable UUID storeId,
            @RequestParam UUID userId
    ) {
        return storeService.getStoreDetails(storeId, userId);
    }

    @GetMapping
    public ResponseEntity<?> getAllStores(@RequestParam UUID userId) {
        List<?> stores = storeService.getAllStores(userId);
        return ResponseEntity.ok(stores);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable UUID storeId,
                                         @RequestParam UUID userId,
                                         @RequestBody StoreRequestDto updatedStoreDto) {
        try {
            storeService.updateStore(storeId, userId, updatedStoreDto);
            return ResponseEntity.ok(new ApiResDto("가게 정보가 성공적으로 수정되었습니다.", 200));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResDto("수정 권한이 없습니다.", 403));
        }
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResDto> deleteStore(
            @PathVariable UUID storeId,
            @RequestParam UUID userId
    ) {
        try {
            storeService.deleteStore(storeId, userId);
            return ResponseEntity.ok(new ApiResDto("가게 삭제가 완료되었습니다.", 200));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResDto("삭제 권한이 없습니다.", 403));
        }
    }
}
