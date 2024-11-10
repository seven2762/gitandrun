package com.sparta.gitandrun.store.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.store.dto.StoreResponseDto;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // 가게 생성 (ADMIN, OWNER, MASTER만 가능)
    @PostMapping
    public ResponseEntity<?> createStore(
            @RequestParam UUID userId,
            @RequestBody Store newStore
    ) {
        try {
            Store createdStore = storeService.createStore(userId, newStore);
            return ResponseEntity.ok(createdStore);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResDto("생성 권한이 없습니다.", 403));
        }
    }



    // 가게 조회 (역할에 따라 필드 필터링)
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreDetails(
            @PathVariable UUID storeId,
            @RequestParam UUID userId
    ) {
        return storeService.getStoreDetails(storeId, userId);
    }

    // 전체 가게 조회
    @GetMapping()
    public ResponseEntity<?> getAllStores(@RequestParam UUID userId) {
        List<?> stores = storeService.getAllStores(userId);
        return ResponseEntity.ok(stores);
    }

    // 가게 수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable UUID storeId,
                                         @RequestParam UUID userId,
                                         @RequestBody Store updatedStore) {
        try {
            storeService.updateStore(storeId, userId, updatedStore);
            return ResponseEntity.ok(new ApiResDto("가게 정보가 성공적으로 수정되었습니다.", 200));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResDto("수정 권한이 없습니다.", 403));
        }
    }

    // 가게 삭제 (soft delete)
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
