package com.sparta.gitandrun.store.dto;

import com.sparta.gitandrun.store.entity.Store;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FullStoreResponse {
    private UUID storeId;
    private UUID categoryId;
    private String storeName;
    private String phone;
    private String category;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String deletedBy;
    private LocalDateTime deletedAt;
    private String address;
    private String addressDetail;
    private String zipCode;
    private Long userId;  // user 대신 userId만 포함

    public FullStoreResponse(Store store) {
        this.storeId = store.getStoreId();
        this.categoryId = store.getCategory().getUuid();
        this.storeName = store.getStoreName();
        this.phone = store.getPhone();
        this.category = store.getCategory().toString();
        this.createdBy = store.getCreatedBy();
        this.createdAt = store.getCreatedAt();
        this.updatedBy = store.getUpdatedBy();
        this.updatedAt = store.getUpdatedAt();
        this.deletedBy = store.getDeletedBy();
        this.deletedAt = store.getDeletedAt();
        this.address = store.getAddress();
        this.addressDetail = store.getAddressDetail();
        this.zipCode = store.getZipCode();
        this.userId = store.getUser().getUserId();  // User 객체 대신 userId만 반환
    }
}
