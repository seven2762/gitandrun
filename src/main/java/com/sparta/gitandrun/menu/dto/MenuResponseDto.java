package com.sparta.gitandrun.menu.dto;

import com.sparta.gitandrun.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

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

    private List<MenuResponseDto> menuList = new ArrayList<>();

    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuContent = menu.getMenuContent();
        this.createdAt = menu.getCreatedAt();
        this.createdBy = "test create user";
        this.updatedAt = menu.getUpdatedAt();
        this.updatedBy = "test update user";
//        this.isDeleted = menu.getIsDeleted();
//        this.deletedAt = menu.getDeletedAt();
//        this.deletedBy = menu.getDeletedBy();
    }

}
