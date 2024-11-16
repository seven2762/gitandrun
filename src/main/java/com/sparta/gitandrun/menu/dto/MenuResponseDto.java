package com.sparta.gitandrun.menu.dto;

import com.sparta.gitandrun.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MenuResponseDto {

    private UUID storeId;
    private UUID menuId;
    private String menuName;
    private int menuPrice;
    private String menuContent;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
    LocalDateTime localDateTime = LocalDateTime.now();

    public MenuResponseDto(Menu menu) {
        this.storeId = menu.getStore().getStoreId();
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuContent = menu.getMenuContent();
        this.createdAt = menu.getCreatedAt();
        this.createdBy = menu.getCreatedBy();
        this.updatedAt = menu.getUpdatedAt();
        this.updatedBy = menu.getUpdatedBy();
    }
}
