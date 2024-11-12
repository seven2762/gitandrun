package com.sparta.gitandrun.store.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.gitandrun.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "p_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @Column(name = "store_id", columnDefinition = "UUID")
    private UUID storeId;

    @Column(name = "category_id", nullable = false, columnDefinition = "UUID")
    private UUID categoryId;

    @Column(name = "store_name", nullable = false, length = 100, unique = true)
    private String storeName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", nullable = false, length = 100)
    private String updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "address_detail", nullable = false, length = 255)
    private String addressDetail;

    @Column(name = "zip_code", nullable = false, length = 255)
    private String zipCode;

    // User와의 ManyToOne 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // Store 테이블에 user_id 컬럼 추가
    private User user;
    

    @PrePersist
    public void prePersist() {
        if (this.storeId == null) {
            this.storeId = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.category != null) {
            this.categoryId = this.category.getUuid();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsDeleted(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            this.categoryId = category.getUuid();  // 카테고리의 UUID를 자동 설정
        }
    }
}
