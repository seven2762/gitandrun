package com.sparta.gitandrun.store.dto;

import com.sparta.gitandrun.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StoreRequestDto {

    @NotBlank(message = "Store name is required")
    private String storeName;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotNull(message = "Category is required")
    private Category category;

    @NotBlank(message = "Address is required")
    private String address;

    private String addressDetail;

    @NotBlank(message = "Zip code is required")
    private String zipCode;

    // Getters and setters
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
