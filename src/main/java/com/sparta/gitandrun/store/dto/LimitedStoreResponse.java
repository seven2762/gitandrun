package com.sparta.gitandrun.store.dto;

import com.sparta.gitandrun.store.entity.Store;
import lombok.Data;

@Data
public class LimitedStoreResponse {
    private String storeName;
    private String phone;
    private String category;
    private String address;
    private String addressDetail;
    private String zipCode;

    public LimitedStoreResponse(Store store) {
        this.storeName = store.getStoreName();
        this.phone = store.getPhone();
        this.category = store.getCategory().toString();
        this.address = store.getAddress();
        this.addressDetail = store.getAddressDetail();
        this.zipCode = store.getZipCode();
    }
}
