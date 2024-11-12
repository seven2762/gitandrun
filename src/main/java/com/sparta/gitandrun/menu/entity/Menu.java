package com.sparta.gitandrun.menu.entity;

import com.sparta.gitandrun.common.entity.BaseEntity;
import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_menu")
@NoArgsConstructor
@SoftDelete(columnName = "is_deleted")
public class Menu extends BaseEntity {

    @Id
    @Column(name = "menuId", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID menuId;

    @Column(name = "menuName", nullable = false, length = 20)
    private String menuName;

    @Column(name = "menuPrice", nullable = false)
    private int menuPrice;

    @Column(name = "menuContent", nullable = false, length = 100)
    private String menuContent;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false, length = 100)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false, insertable = false, updatable = false)
    private boolean isDeleted;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = true, length = 100)
    private String deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

//    @PrePersist
//    public void PrePersist() {
//        UUID uuid = UUID.randomUUID();
//    }

    public Menu(MenuRequestDto requestDto) {
            this.menuName = requestDto.getMenuName();
            this.menuPrice = requestDto.getMenuPrice();
            this.menuContent = requestDto.getMenuContent();
            this.createdAt = requestDto.getCreatedAt();
            this.createdBy = "test created user";
            this.updatedAt = requestDto.getUpdatedAt();
            this.updatedBy = "test updated user";
            this.isDeleted = Boolean.FALSE;
            this.deletedAt = requestDto.getDeletedAt();
            this.deletedBy = "test deleted User";
        }

    public void update(MenuRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.menuPrice = requestDto.getMenuPrice();
        this.menuContent = requestDto.getMenuContent();
    }
}
