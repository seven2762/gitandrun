package com.sparta.gitandrun.store.dto;

import com.sparta.gitandrun.store.entity.Store;
import lombok.Data;

import java.util.UUID;

@Data
public class LimitedStoreResponse {
    private UUID storeId; // storeId 필드 추가
    private String storeName;
    private String phone;
    private String category;
    private String address;
    private String addressDetail;
    private String zipCode;
    private Long userId;

    public LimitedStoreResponse(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.phone = store.getPhone();
        this.category = store.getCategory().toString();
        this.address = store.getAddress().getAddress();  // Address 필드 사용
        this.addressDetail = store.getAddress().getAddressDetail();  // Address 필드 사용
        this.zipCode = store.getAddress().getZipCode();  // Address 필드 사용
        this.userId = store.getUser().getUserId();
    }
}
